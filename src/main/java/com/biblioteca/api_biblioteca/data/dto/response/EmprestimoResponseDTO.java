package com.biblioteca.api_biblioteca.data.dto.response;

import com.biblioteca.api_biblioteca.data.entity.*;

public record EmprestimoResponseDTO(

    Long id,

    Pessoa pessoa,

    Livro livro
){
    public EmprestimoResponseDTO(Emprestimo emprestimo){
        this(emprestimo.getIdEmprestimo(), emprestimo.getPessoa(), emprestimo.getLivro());
    }
}
