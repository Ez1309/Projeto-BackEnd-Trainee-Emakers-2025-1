package com.biblioteca.api_biblioteca.exceptions.pessoa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.CONFLICT)
public class CpfJaCadastradoException extends OperacaoInvalidaException {
    
    public CpfJaCadastradoException(String message){
        super(message);
    }
}
