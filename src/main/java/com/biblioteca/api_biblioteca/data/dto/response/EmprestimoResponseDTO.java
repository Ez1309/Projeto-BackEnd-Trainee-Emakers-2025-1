package com.biblioteca.api_biblioteca.data.dto.response;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.Emprestimo;

public record EmprestimoResponseDTO(

    Long id,

    PessoaResponseDTO pessoaResponseDTO,

    LivroResponseDTO livroResponseDTO,

    LocalDate dataEmprestimo,

    LocalDate dataDevolucao
){
    public EmprestimoResponseDTO(Emprestimo emprestimo){
        this(emprestimo.getIdEmprestimo(), new PessoaResponseDTO(emprestimo.getPessoa()), new LivroResponseDTO(emprestimo.getLivro()), emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
    }
}
