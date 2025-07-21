package com.biblioteca.api_biblioteca.data.dto.response;

import com.biblioteca.api_biblioteca.data.entity.Pessoa;

public record PessoaResponseDTO(

    Long id,

    String name,

    String cpf,

    String cep,

    String email,

    String senha
){
    public PessoaResponseDTO(Pessoa pessoa){
        this(pessoa.getIdPessoa(), pessoa.getNome(), pessoa.getCpf(), pessoa.getCep(), pessoa.getEmail(), pessoa.getSenha());
    }
}
