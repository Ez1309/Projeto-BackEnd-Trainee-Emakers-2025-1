package com.biblioteca.api_biblioteca.exceptions.livro;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;

@ResponseStatus(HttpStatus.CONFLICT)
public class LivroDuplicadoException extends OperacaoInvalidaException{
    
    public LivroDuplicadoException(String message){
        super(message);
    }
}
