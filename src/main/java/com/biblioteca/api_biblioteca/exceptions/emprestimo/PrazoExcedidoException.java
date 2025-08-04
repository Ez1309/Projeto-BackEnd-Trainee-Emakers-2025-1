package com.biblioteca.api_biblioteca.exceptions.emprestimo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PrazoExcedidoException extends OperacaoInvalidaException {

    public PrazoExcedidoException(String message) {
        super(message);
    }
    
}
