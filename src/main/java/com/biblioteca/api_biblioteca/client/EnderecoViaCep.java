package com.biblioteca.api_biblioteca.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EnderecoViaCep(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro) {

}
