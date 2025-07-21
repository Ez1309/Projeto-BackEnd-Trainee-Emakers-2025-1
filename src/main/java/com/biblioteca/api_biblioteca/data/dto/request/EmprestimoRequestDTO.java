package com.biblioteca.api_biblioteca.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import com.biblioteca.api_biblioteca.data.entity.*;

public record EmprestimoRequestDTO(

    @NotBlank(message = "Pessoa é obrigatória")
    Pessoa pessoa,

    @NotBlank(message = "Livro é obrigatório")
    Livro livro
) {
}
