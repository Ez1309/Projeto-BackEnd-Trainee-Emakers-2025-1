package com.biblioteca.api_biblioteca.data.dto.request;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Value;

import com.biblioteca.api_biblioteca.data.entity.PessoaRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "O campo nome não pode ultrapassar {max} caracteres")
    String nome,

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos, sem pontos ou traços")
    @CPF(message = "CPF Inválido")
    String cpf,

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve estar no formato 00000-000 ou 00000000")
    String cep,

    String rua,

    String bairro,

    String cidade,

    String estado,

    @NotBlank(message = "Email é obrigatório")
    @Size(max = 100, message = "O campo email não pode ultrapassar {max} caracteres")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Size(max = 100, message = "O campo senha não pode ultrapassar {max} caracteres")
    String senha,

    @Value("user")
    PessoaRole role
) {
    
}
