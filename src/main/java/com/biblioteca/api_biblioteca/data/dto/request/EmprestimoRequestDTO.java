package com.biblioteca.api_biblioteca.data.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.Livro;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.fasterxml.jackson.annotation.JsonFormat;

public record EmprestimoRequestDTO(

    @NotNull(message = "Pessoa é obrigatória")
    Pessoa pessoa,

    @NotNull(message = "Livro é obrigatório")
    Livro livro,

    @NotNull(message = "Data de empréstimo é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent(message = "A data do empréstimo não pode estar no passado")
    LocalDate dataEmprestimo,
     
    @NotNull(message = "Data de devolução é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent(message = "A data de devolução não pode estar no passado")
    LocalDate dataDevolucao


) {
}
