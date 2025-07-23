package com.biblioteca.api_biblioteca.exceptions.general;

public class EntityNotFoundException extends RuntimeException{
    
    public EntityNotFoundException(Long id){
        super("Entity not found with id: " + id);
    }
}
