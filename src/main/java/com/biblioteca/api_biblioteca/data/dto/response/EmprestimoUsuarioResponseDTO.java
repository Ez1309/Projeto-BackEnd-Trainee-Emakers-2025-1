package com.biblioteca.api_biblioteca.data.dto.response;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.biblioteca.api_biblioteca.data.entity.Emprestimo;
import com.biblioteca.api_biblioteca.data.enums.StatusEmprestimo;

// Este DTO é um "recibo" para o usuário.
public record EmprestimoUsuarioResponseDTO(
    Long idEmprestimo, // O ID do empréstimo é útil caso ele queira cancelar/devolver
    String nomeLivro,
    String autorLivro,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataEmprestimo,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataDevolucaoAgendada,
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dataDevolucaoReal,
    StatusEmprestimo status
) {
    public EmprestimoUsuarioResponseDTO(Emprestimo emprestimo) {
        this(
            emprestimo.getIdEmprestimo(),
            emprestimo.getLivro().getNome(),
            emprestimo.getLivro().getAutor(),
            emprestimo.getDataEmprestimo(),
            emprestimo.getDataDevolucaoAgendada(),
            emprestimo.getDataDevolucaoReal(),
            emprestimo.getStatus()
        );
    }
}