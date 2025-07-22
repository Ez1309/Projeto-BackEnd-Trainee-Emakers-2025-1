package com.biblioteca.api_biblioteca.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.api_biblioteca.data.dto.request.PessoaRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.PessoaResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;


@Service
public class PessoaService {
    
    @Autowired
    private PessoaRepository pessoaRepository;

    public List<PessoaResponseDTO> getAllPessoas(){
        List<Pessoa> pessoas = pessoaRepository.findAll();

        return pessoas.stream().map(PessoaResponseDTO::new).collect(Collectors.toList());
    }

    public PessoaResponseDTO getPessoaById(Long idPessoa){
        Pessoa pessoa = pessoaRepository.findById(idPessoa).orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

        return new PessoaResponseDTO(pessoa);
    }

    public PessoaResponseDTO criarPessoa(PessoaRequestDTO pessoaRequestDTO){
        Pessoa pessoa = new Pessoa(pessoaRequestDTO);
        pessoaRepository.save(pessoa);

        return new PessoaResponseDTO(pessoa);
    }

    public PessoaResponseDTO atualizarPessoa(Long idPessoa, PessoaRequestDTO pessoaRequestDTO){
        
        Pessoa pessoa = getPessoaEntityById(idPessoa);

        pessoa.setNome(pessoaRequestDTO.nome());
        pessoa.setCpf(pessoaRequestDTO.cpf());
        pessoa.setCep(pessoaRequestDTO.cep());
        pessoa.setEmail(pessoaRequestDTO.email());
        pessoa.setSenha(pessoaRequestDTO.senha());
        
        pessoaRepository.save(pessoa);

        return new PessoaResponseDTO(pessoa);
    }

    public String deletarPessoa(Long idPessoa){
        
        Pessoa pessoa = getPessoaEntityById(idPessoa);

        pessoaRepository.delete(pessoa);

        return "Pessoa id: " + idPessoa + " deletada";
    }

    private Pessoa getPessoaEntityById(Long idPessoa){
        return pessoaRepository.findById(idPessoa).orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
    }
}
