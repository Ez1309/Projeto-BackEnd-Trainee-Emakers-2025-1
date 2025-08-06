package com.biblioteca.api_biblioteca.data.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequestDTO(

        @NotBlank(message = "A senha atual é obrigatória") String senhaAtual,

        @NotBlank(message = "A nova senha é obrigatória") String novaSenha) {

}
