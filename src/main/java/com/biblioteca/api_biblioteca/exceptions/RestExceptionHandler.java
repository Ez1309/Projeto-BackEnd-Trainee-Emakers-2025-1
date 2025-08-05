package com.biblioteca.api_biblioteca.exceptions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.biblioteca.api_biblioteca.exceptions.autenticacao.EmailJaCadastradoException;
import com.biblioteca.api_biblioteca.exceptions.autenticacao.ErroGeracaoTokenExcecao;
import com.biblioteca.api_biblioteca.exceptions.emprestimo.LivroIndisponivelException;
import com.biblioteca.api_biblioteca.exceptions.general.EntidadeNaoEncontradaException;
import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;
import com.biblioteca.api_biblioteca.exceptions.livro.LivroDuplicadoException;
import com.biblioteca.api_biblioteca.exceptions.pessoa.CpfJaCadastradoException;
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

    @ExceptionHandler(LivroIndisponivelException.class)
    public ResponseEntity<RestErrorMessage> handleLivroIndisponivel (LivroIndisponivelException exception){
        return criarRespostaErro(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ErroGeracaoTokenExcecao.class)
    public ResponseEntity<RestErrorMessage> handleErroGeracaoToken(ErroGeracaoTokenExcecao exception){
        return criarRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno ao processar a autenticação.");
    }

    @ExceptionHandler({LivroDuplicadoException.class, EmailJaCadastradoException.class, CpfJaCadastradoException.class})
    public ResponseEntity<RestErrorMessage> handleRecursoDuplicado(RuntimeException exception) {
        return criarRespostaErro(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestErrorMessage> handleBadCredential(BadCredentialsException exception){
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
    public ResponseEntity<RestErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        
        String message = "A requisição contém um formato inválido. Verifique os dados enviados.";
        
        if (exception.getCause() instanceof InvalidFormatException ifx){
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handleCredenciaisInvalidas(){
        return criarRespostaErro(HttpStatus.UNAUTHORIZED, "Credenciais inválidas. Verifique o email e a senha.");
    }
}
