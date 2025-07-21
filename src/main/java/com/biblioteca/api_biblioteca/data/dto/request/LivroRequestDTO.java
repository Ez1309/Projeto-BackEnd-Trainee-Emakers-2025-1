package com.biblioteca.api_biblioteca.data.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record LivroRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @NotBlank(message = "Autor é obrigatório")
    String autor,

    @NotNull(message = "Data de lançamento é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "A data de lançamento não pode estar no futuro")
    LocalDate dataLancamento

){  
}
