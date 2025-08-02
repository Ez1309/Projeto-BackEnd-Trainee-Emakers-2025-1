package com.biblioteca.api_biblioteca.data.dto.response;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.Emprestimo;
import com.biblioteca.api_biblioteca.data.entity.StatusEmprestimo;

public record EmprestimoResponseDTO(

    Long id,

    PessoaResponseDTO pessoaResponseDTO,

    LivroResponseDTO livroResponseDTO,

    LocalDate dataEmprestimo,

    LocalDate dataDevolucaoAgendada,

    LocalDate dataDevolucaoReal,

    StatusEmprestimo status
){
    public EmprestimoResponseDTO(Emprestimo emprestimo){
        this(emprestimo.getIdEmprestimo(), new PessoaResponseDTO(emprestimo.getPessoa()), new LivroResponseDTO(emprestimo.getLivro()), emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucaoAgendada(), emprestimo.getDataDevolucaoReal(), emprestimo.getStatus());
    }
}
