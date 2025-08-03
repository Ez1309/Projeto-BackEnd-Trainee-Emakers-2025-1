package com.biblioteca.api_biblioteca.data.dto.response;

import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.data.enums.PessoaRole;

public record PessoaResponseDTO(

    Long id,

    String name,

    String cpf,

    String cep,

    String rua,

    String bairro,

    String cidade,

    String estado,

    String email,

    PessoaRole papel
){
    public PessoaResponseDTO(Pessoa pessoa){
        this(pessoa.getIdPessoa(), pessoa.getNome(), pessoa.getCpf(), pessoa.getCep(), pessoa.getRua(), pessoa.getBairro(), pessoa.getCidade(), pessoa.getEstado(), pessoa.getEmail(), pessoa.getRole());
    }
}
