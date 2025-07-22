package com.biblioteca.api_biblioteca.data.dto.request;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PessoaRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF Inválido")
    String cpf,

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve estar no formato 00000-000 ou 00000000")
    String cep,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    String senha
) {
    
}
