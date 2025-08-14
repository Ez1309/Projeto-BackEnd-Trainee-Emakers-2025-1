package com.biblioteca.api_biblioteca.data.dto.request;

import jakarta.validation.constraints.NotNull;

public record AtualizarEstoqueRequestDTO(

    @NotNull(message = "A quantidade a ser ajustada é obrigatória")
    Integer quantidade
){
    
}
