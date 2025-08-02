package com.biblioteca.api_biblioteca.exceptions.general;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.biblioteca.api_biblioteca.exceptions.RestErrorMessage;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;


@ControllerAdvice
public class GeneralExceptionHandler{
    
    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<RestErrorMessage> EntityNotFoundHandler(EntityNotFoundException exception){

        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(errorMessage.status()).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<List<RestErrorMessage>> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        List<RestErrorMessage> errors = exception.getBindingResult().getFieldErrors().stream().map(fieldError -> new RestErrorMessage(HttpStatus.BAD_REQUEST, fieldError.getDefaultMessage())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        Map<String, String> errors = new HashMap<>();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldPath = invalidFormatException.getPath()
                .stream()
                .map(Reference ::getFieldName)
                .collect(Collectors.joining("."));

            Object value = invalidFormatException.getValue();

            if (invalidFormatException.getTargetType().equals(java.time.LocalDate.class)) {
                errors.put(fieldPath, "Data inválida para o campo '" + fieldPath + "': " + value +
                        ". Use o formato dd/MM/yyyy.");
            } else {
                errors.put(fieldPath, "Valor inválido para o campo '" + fieldPath + "': \"" + value + "\".");
            }
        } else {
            errors.put("mensagem", "Erro de leitura na requisição: verifique os dados enviados.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Este método será chamado sempre que uma BusinessException for lançada em qualquer controller
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        // Criamos um corpo de erro JSON personalizado
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", "Bad Request",
            "message", ex.getMessage() // A mensagem que definimos no serviço
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
