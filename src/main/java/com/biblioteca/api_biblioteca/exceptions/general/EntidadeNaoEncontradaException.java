package com.biblioteca.api_biblioteca.exceptions.general;

public class EntidadeNaoEncontradaException extends RuntimeException{
    
    public EntidadeNaoEncontradaException(Long id){
        super("Não foi encontrada a entidade com o id: " + id);
    }
}
