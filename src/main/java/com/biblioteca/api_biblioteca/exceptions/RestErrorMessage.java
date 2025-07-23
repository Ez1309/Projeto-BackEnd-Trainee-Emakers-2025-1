package com.biblioteca.api_biblioteca.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;

public record RestErrorMessage(
    HttpStatus status,
    String message,
    Date timestamp
) {
    public RestErrorMessage(HttpStatus status, String message){
        this(status, message, new Date());
    }
}
