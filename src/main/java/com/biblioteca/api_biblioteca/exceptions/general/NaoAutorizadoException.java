package com.biblioteca.api_biblioteca.exceptions.general;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NaoAutorizadoException extends OperacaoInvalidaException {

    public NaoAutorizadoException(String message) {
        super(message);
    }
}
