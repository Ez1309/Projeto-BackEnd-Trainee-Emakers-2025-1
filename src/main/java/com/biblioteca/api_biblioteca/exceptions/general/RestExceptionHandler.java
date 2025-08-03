package com.biblioteca.api_biblioteca.exceptions.general;

import java.time.LocalDate;
import java.util.List;
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
public class RestExceptionHandler{

    // Método genérico para criar uma resposta de erro personalizada
    private ResponseEntity<RestErrorMessage> criarRespostaErro(HttpStatus status, String message){

        RestErrorMessage mensagemErro = new RestErrorMessage(status, message);
        return ResponseEntity.status(status).body(mensagemErro);
    }
    
    // Entidade não encontrada retorna erro 404
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    private ResponseEntity<RestErrorMessage> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException exception){
        return criarRespostaErro(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(OperacaoInvalidaException.class)
    private ResponseEntity<RestErrorMessage> handleOperacaoInvalida (OperacaoInvalidaException exception){
        return criarRespostaErro(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    // 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<List<RestErrorMessage>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<RestErrorMessage> errors = exception
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> new RestErrorMessage(HttpStatus.BAD_REQUEST, fieldError.getDefaultMessage()))
        .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        
        String message = "A requisição contém um formato inválido. Verifique os dados enviados.";
        
        if (ex.getCause() instanceof InvalidFormatException ifx){
            if (ifx.getTargetType().equals(LocalDate.class)) {
                String fieldName = ifx.getPath().stream()
                                    .map(Reference::getFieldName)
                                    .collect(Collectors.joining("."));
                message = String.format(
                    "O campo '%s' contém uma data no formato inválido. Use o formado dd/MM/yyyy.", fieldName
                );
            }
        }

        return criarRespostaErro(HttpStatus.BAD_REQUEST, message);
        
    }
}
