package com.biblioteca.api_biblioteca.exceptions.autenticacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailJaCadastradoException extends OperacaoInvalidaException {

    public EmailJaCadastradoException(String message) {
        super(message);
    }
}
