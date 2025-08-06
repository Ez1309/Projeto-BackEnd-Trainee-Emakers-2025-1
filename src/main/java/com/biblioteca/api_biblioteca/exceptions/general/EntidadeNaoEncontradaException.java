package com.biblioteca.api_biblioteca.exceptions.general;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(Long id) {
        super("Não foi encontrada a entidade com o ID: " + id);
    }
}
