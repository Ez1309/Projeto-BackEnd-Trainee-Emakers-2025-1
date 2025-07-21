package com.biblioteca.api_biblioteca.data.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;

public record LivroRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @NotBlank(message = "Autor é obrigatório")
    String autor,

    @NotBlank(message = "Data de lançamento é obrigatória")
    LocalDate dataLancamento

){  
}
