package com.biblioteca.api_biblioteca.exceptions.emprestimo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.CONFLICT)
public class LivroIndisponivelException extends OperacaoInvalidaException {

    public LivroIndisponivelException(String message) {
        super(message);
    }
}