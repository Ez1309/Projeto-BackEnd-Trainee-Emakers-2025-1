package com.biblioteca.api_biblioteca.exceptions.autenticacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SenhaInvalidaException extends OperacaoInvalidaException{
    public SenhaInvalidaException(String message){
        super(message);
    }
}
