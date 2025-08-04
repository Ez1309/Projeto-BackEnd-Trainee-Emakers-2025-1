package com.biblioteca.api_biblioteca.exceptions.autenticacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Herdamos diretamente de RuntimeException, pois este é um erro interno do servidor (500)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErroGeracaoTokenExcecao extends RuntimeException {

    public ErroGeracaoTokenExcecao(String message, Throwable cause) {
        super(message, cause);
    }
}