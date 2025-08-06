package com.biblioteca.api_biblioteca.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteca.api_biblioteca.client.EnderecoViaCep;
import com.biblioteca.api_biblioteca.client.ViaCepClient;
import com.biblioteca.api_biblioteca.data.dto.request.AlterarSenhaRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.request.RegisterRequestDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.exceptions.autenticacao.EmailJaCadastradoException;
import com.biblioteca.api_biblioteca.exceptions.general.NaoAutorizadoException;
import com.biblioteca.api_biblioteca.exceptions.pessoa.CepInvalidoException;
import com.biblioteca.api_biblioteca.exceptions.pessoa.CpfJaCadastradoException;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ViaCepClient viaCepClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
    }

    @Transactional
    public void registrar(RegisterRequestDTO registerDTO) {

        // Verifica se o usuário já existe no banco
        if (pessoaRepository.existsByEmail(registerDTO.email())) {
            throw new EmailJaCadastradoException("O email informado já está em uso.");
        }

        if (pessoaRepository.existsByCpf(registerDTO.cpf())) {
            throw new CpfJaCadastradoException("O CPF informado já está cadastrado");
        }

        // Resgata o CEP informado
        String cepLimpo = registerDTO.cep().replaceAll("[^\\d]", "");
        EnderecoViaCep endereco = viaCepClient.consultaCep(cepLimpo);

        // Verifica se o CEP informado existe
        if (endereco != null && Boolean.TRUE.equals(endereco.erro())) {
            throw new CepInvalidoException("O CEP informado é inválido.");
        }

        // Criptografa a senha informada
        String senhaCriptografada = passwordEncoder.encode(registerDTO.senha());

        // Cria a pessoa usando o contrutor que define o role USER por padrão
        Pessoa novaPessoa = new Pessoa(registerDTO.nome(), registerDTO.cpf(), registerDTO.cep(), registerDTO.email(),
                senhaCriptografada);

        // Preenchimento os dados complementares do endereço
        if (endereco != null) {
            novaPessoa.setRua(endereco.logradouro());
            novaPessoa.setBairro(endereco.bairro());
            novaPessoa.setCidade(endereco.localidade());
            novaPessoa.setEstado(endereco.uf());
        }

        // Salva a pessoa no banco
        pessoaRepository.save(novaPessoa);
    }

    @Transactional
    public void alterarSenha(AlterarSenhaRequestDTO alterarSenhaRequestDTO, Pessoa pessoaLogada) {

        if (!passwordEncoder.matches(alterarSenhaRequestDTO.senhaAtual(), pessoaLogada.getSenha())) {
            throw new NaoAutorizadoException("A senha atual informada está incorreta.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(alterarSenhaRequestDTO.novaSenha());
        pessoaLogada.setSenha(novaSenhaCriptografada);

        pessoaRepository.save(pessoaLogada);

    }

}
