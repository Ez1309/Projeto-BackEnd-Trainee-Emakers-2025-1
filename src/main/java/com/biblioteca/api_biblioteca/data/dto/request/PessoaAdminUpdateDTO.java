package com.biblioteca.api_biblioteca.data.dto.request;

import com.biblioteca.api_biblioteca.data.enums.PessoaRole;

public record PessoaAdminUpdateDTO(

        String nome,

        String cpf,

        String email,

        String cep,

        PessoaRole role

) {
}
