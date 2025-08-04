package com.biblioteca.api_biblioteca.data.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
    
    @NotBlank(message = "O email é obrigatório")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    String senha

) {
} 