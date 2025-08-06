package com.biblioteca.api_biblioteca.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RestErrorMessage(

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime timestamp,
        int status,
        String error,
        String message) {
    public RestErrorMessage(HttpStatus status, String message) {
        this(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message);
    }
}
