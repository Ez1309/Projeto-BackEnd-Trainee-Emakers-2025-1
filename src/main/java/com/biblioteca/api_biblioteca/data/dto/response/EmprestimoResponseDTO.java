package com.biblioteca.api_biblioteca.data.dto.response;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.*;

public record EmprestimoResponseDTO(

    Long id,

    Pessoa pessoa,

    Livro livro,

    LocalDate dataEmprestimo,

    LocalDate dataDevolucao
){
    public EmprestimoResponseDTO(Emprestimo emprestimo){
        this(emprestimo.getIdEmprestimo(), emprestimo.getPessoa(), emprestimo.getLivro(), emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
    }
}
