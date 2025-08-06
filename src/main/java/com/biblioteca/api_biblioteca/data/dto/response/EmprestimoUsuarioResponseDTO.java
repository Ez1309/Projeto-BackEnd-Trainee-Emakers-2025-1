package com.biblioteca.api_biblioteca.data.dto.response;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.Emprestimo;
import com.biblioteca.api_biblioteca.data.enums.StatusEmprestimo;
import com.fasterxml.jackson.annotation.JsonFormat;


public record EmprestimoUsuarioResponseDTO(
        Long idEmprestimo, 
        String nomeLivro,
        String autorLivro,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataEmprestimo,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataDevolucaoAgendada,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataDevolucaoReal,
        StatusEmprestimo status) {
    public EmprestimoUsuarioResponseDTO(Emprestimo emprestimo) {
        this(
                emprestimo.getIdEmprestimo(),
                emprestimo.getLivro().getNome(),
                emprestimo.getLivro().getAutor(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucaoAgendada(),
                emprestimo.getDataDevolucaoReal(),
                emprestimo.getStatus());
    }
}