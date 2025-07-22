package com.biblioteca.api_biblioteca.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import com.biblioteca.api_biblioteca.data.entity.Livro;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;

public record EmprestimoRequestDTO(

    @NotBlank(message = "Pessoa é obrigatória")
    Pessoa pessoa,

    @NotBlank(message = "Livro é obrigatório")
    Livro livro,

    @NotBlank(message = "Data de empréstimo é obrigatória")
    LocalDate dataEmprestimo,
     
    @NotBlank(message = "Data de devolução é obrigatória")
    LocalDate dataDevolucao


) {
}
