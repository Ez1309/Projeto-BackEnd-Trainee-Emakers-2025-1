package com.biblioteca.api_biblioteca.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.api_biblioteca.client.EnderecoViaCep;
import com.biblioteca.api_biblioteca.client.ViaCepClient;
import com.biblioteca.api_biblioteca.data.dto.request.PessoaAdminUpdateDTO;
import com.biblioteca.api_biblioteca.data.dto.request.PessoaRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.PessoaResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.exceptions.autenticacao.EmailJaCadastradoException;
import com.biblioteca.api_biblioteca.exceptions.general.EntidadeNaoEncontradaException;
import com.biblioteca.api_biblioteca.exceptions.pessoa.CepInvalidoException;
import com.biblioteca.api_biblioteca.exceptions.pessoa.CpfJaCadastradoException;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;


@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ViaCepClient viaCepClient;

    public List<PessoaResponseDTO> getAllPessoas(){
        List<Pessoa> pessoas = pessoaRepository.findAll();

        return pessoas.stream().map(PessoaResponseDTO::new).collect(Collectors.toList());
    }

    public PessoaResponseDTO getPessoaById(Long idPessoa){
        Pessoa pessoa = getPessoaEntityById(idPessoa);
        return new PessoaResponseDTO(pessoa);
    }

    public PessoaResponseDTO criarPessoa(PessoaRequestDTO pessoaRequestDTO){

        if (pessoaRepository.existsByEmail(pessoaRequestDTO.email())){
            throw new EmailJaCadastradoException("O email informado já está em uso.");
        }

        if (pessoaRepository.existsByCpf(pessoaRequestDTO.cpf())){
            throw new CpfJaCadastradoException("O CPF informado já está em uso.");
        }

        String cepLimpo = pessoaRequestDTO.cep().replaceAll("[^\\d]", "");
        EnderecoViaCep endereco = viaCepClient.consultaCep(cepLimpo);

        // A lógica de validação continua a mesma
        if (endereco != null && Boolean.TRUE.equals(endereco.erro())) {
            throw new CepInvalidoException("O CEP informado é inválido.");
        }

        Pessoa pessoaNova = new Pessoa(pessoaRequestDTO);

        pessoaNova.setSenha(null);

        if (endereco != null) {
            pessoaNova.setRua(endereco.logradouro());
            pessoaNova.setBairro(endereco.bairro());
            pessoaNova.setCidade(endereco.localidade());
            pessoaNova.setEstado(endereco.uf());
        }

        pessoaRepository.save(pessoaNova);

        return new PessoaResponseDTO(pessoaNova);
    }

    public PessoaResponseDTO atualizarPessoaAdmin(Long idPessoa, PessoaAdminUpdateDTO pessoaAdminUpdateDTO){
        
        Pessoa pessoaAtualizar = getPessoaEntityById(idPessoa);

        EnderecoViaCep endereco = null;

        if(pessoaAdminUpdateDTO.email() != null){
            Optional<Pessoa> pessoaComMesmoEmail = pessoaRepository.findByEmail(pessoaAdminUpdateDTO.email());
            if(pessoaComMesmoEmail.isPresent() && !pessoaComMesmoEmail.get().getIdPessoa().equals(idPessoa)){
                throw new EmailJaCadastradoException("O email informado já está em uso por outro usuário.");
            }
        }

        if(pessoaAdminUpdateDTO.cpf() != null){
            Optional<Pessoa> pessoaComMesmoCpf = pessoaRepository.findByCpf(pessoaAdminUpdateDTO.cpf());
            if (pessoaComMesmoCpf.isPresent() && !pessoaComMesmoCpf.get().getIdPessoa().equals(idPessoa)){
                throw new CpfJaCadastradoException("O CPF informado já está em uso  por outro usuário.");
            }
        }


        if (pessoaAdminUpdateDTO.cep() != null && !pessoaAdminUpdateDTO.cep().isBlank()){
            String cepLimpo = pessoaAdminUpdateDTO.cep().replaceAll("[^\\d]", "");
            endereco = viaCepClient.consultaCep(cepLimpo);
        }

        // A lógica de validação continua a mesma
        if (endereco != null && Boolean.TRUE.equals(endereco.erro())) {
            throw new CepInvalidoException("O CEP informado é inválido.");
        }

        if (pessoaAdminUpdateDTO.nome() != null) pessoaAtualizar.setNome(pessoaAdminUpdateDTO.nome());
        
        if (pessoaAdminUpdateDTO.cpf() != null) pessoaAtualizar.setCpf(pessoaAdminUpdateDTO.cpf());
        if (pessoaAdminUpdateDTO.cep() != null) pessoaAtualizar.setCep(pessoaAdminUpdateDTO.cep());
        
        if (endereco != null){
            pessoaAtualizar.setRua(endereco.logradouro());
            pessoaAtualizar.setBairro(endereco.bairro());
            pessoaAtualizar.setCidade(endereco.localidade());
            pessoaAtualizar.setEstado(endereco.uf());
        }

        if (pessoaAdminUpdateDTO.email() != null) pessoaAtualizar.setEmail(pessoaAdminUpdateDTO.email());
        if (pessoaAdminUpdateDTO.role() != null) pessoaAtualizar.setRole(pessoaAdminUpdateDTO.role());
        
        pessoaRepository.save(pessoaAtualizar);

        return new PessoaResponseDTO(pessoaAtualizar);
    }

    public void deletarPessoa(Long idPessoa){
        
        Pessoa pessoa = getPessoaEntityById(idPessoa);

        pessoaRepository.delete(pessoa);
    }

    private Pessoa getPessoaEntityById(Long idPessoa){
        return pessoaRepository.findById(idPessoa).orElseThrow(() -> new EntidadeNaoEncontradaException(idPessoa));
    }
}
