package com.biblioteca.api_biblioteca.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.api_biblioteca.client.EnderecoViaCep;
import com.biblioteca.api_biblioteca.client.ViaCepClient;
import com.biblioteca.api_biblioteca.data.dto.request.RegisterRequestDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService implements UserDetailsService{

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ViaCepClient viaCepClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return pessoaRepository.findByEmail(email);
    }

    @Transactional
    public void registrar(RegisterRequestDTO registerDTO){

        // Verifica se o usuário já existe no banco
        if (pessoaRepository.findByEmail(registerDTO.email()) != null) {
            throw new OperacaoInvalidaException("O email informado já está em uso.");
        }

        // Resgata o CEP informado
        String cepLimpo = registerDTO.cep().replaceAll("[^\\d]", "");
        EnderecoViaCep endereco = viaCepClient.consultaCep(cepLimpo);

        // Verifica se o CEP informado existe
        if (endereco != null && Boolean.TRUE.equals(endereco.erro())) {
            throw new OperacaoInvalidaException("O CEP informado é inválido.");
        }

        // Criptografa a senha informada
        String senhaCriptografada = passwordEncoder.encode(registerDTO.senha());

        // Cria a pessoa usando o contrutor que define o role USER por padrão
        Pessoa novaPessoa = new Pessoa(registerDTO.nome(), registerDTO.cpf(), registerDTO.cep(), registerDTO.email(), senhaCriptografada);

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
    
}
