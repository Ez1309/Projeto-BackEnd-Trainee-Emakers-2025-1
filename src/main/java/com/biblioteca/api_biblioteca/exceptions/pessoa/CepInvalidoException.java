package com.biblioteca.api_biblioteca.exceptions.pessoa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CepInvalidoException extends OperacaoInvalidaException {

    public CepInvalidoException(String message) {
        super(message);
    }
}
