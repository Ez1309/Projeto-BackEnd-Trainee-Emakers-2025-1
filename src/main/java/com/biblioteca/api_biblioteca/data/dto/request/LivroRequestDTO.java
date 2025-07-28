package com.biblioteca.api_biblioteca.data.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record LivroRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "O campo nome não pode ultrapassar {max} caracteres")
    String nome,

    @NotBlank(message = "Autor é obrigatório")
    @Size(max = 100, message = "O campo autor não pode ultrapassar {max} caracteres")
    String autor,

    @NotNull(message = "Data de lançamento é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "A data de lançamento não pode estar no futuro")
    LocalDate dataLancamento

){  
}
