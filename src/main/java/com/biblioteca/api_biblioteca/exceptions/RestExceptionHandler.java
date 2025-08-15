package com.biblioteca.api_biblioteca.exceptions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.biblioteca.api_biblioteca.exceptions.autenticacao.EmailJaCadastradoException;
import com.biblioteca.api_biblioteca.exceptions.autenticacao.ErroGeracaoTokenExcecao;
import com.biblioteca.api_biblioteca.exceptions.emprestimo.LivroIndisponivelException;
import com.biblioteca.api_biblioteca.exceptions.general.EntidadeNaoEncontradaException;
import com.biblioteca.api_biblioteca.exceptions.general.NaoAutorizadoException;
import com.biblioteca.api_biblioteca.exceptions.general.OperacaoInvalidaException;
import com.biblioteca.api_biblioteca.exceptions.livro.ExclusaoNaoPermitidaException;
import com.biblioteca.api_biblioteca.exceptions.livro.LivroDuplicadoException;
import com.biblioteca.api_biblioteca.exceptions.pessoa.CpfJaCadastradoException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class RestExceptionHandler {

    // Handler para erro 404
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<RestErrorMessage> handleNotFound(RuntimeException ex) {
        return criarRespostaErro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Handler para erros 409
    @ExceptionHandler({
        LivroDuplicadoException.class,
        EmailJaCadastradoException.class,
        CpfJaCadastradoException.class,
        LivroIndisponivelException.class,
        ExclusaoNaoPermitidaException.class,
        DataIntegrityViolationException.class
    })
    public ResponseEntity<RestErrorMessage> handleConflict(RuntimeException ex) {
        String message = (ex instanceof DataIntegrityViolationException)
                ? "Conflito de dados. O recurso pode estar sendo usado em outro lugar."
                : ex.getMessage();
        return criarRespostaErro(HttpStatus.CONFLICT, message);
    }

    // Handler para erros 401
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, NaoAutorizadoException.class})
    public ResponseEntity<RestErrorMessage> handleUnauthorized(RuntimeException ex) {
    
        if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
            return criarRespostaErro(HttpStatus.UNAUTHORIZED, "Credenciais inválidas. Verifique o e-mail e a senha.");
        }

        return criarRespostaErro(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    
    // Handler para erro 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestErrorMessage> handleAccessDenied(AccessDeniedException ex) {
        return criarRespostaErro(HttpStatus.FORBIDDEN, "Acesso negado. Você não tem permissão para executar esta operação.");
    }
    
    // Handlers para erros 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<RestErrorMessage>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<RestErrorMessage> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new RestErrorMessage(HttpStatus.BAD_REQUEST, fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "A requisição contém um formato inválido. Verifique os dados enviados.";
        if (ex.getCause() instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType() != null && ifx.getTargetType().equals(LocalDate.class)) {
                String fieldName = ifx.getPath().stream().map(Reference::getFieldName).collect(Collectors.joining("."));
                message = String.format("O campo '%s' contém uma data no formato inválido. Use o formato dd/MM/yyyy.", fieldName);
            }
        }
        return criarRespostaErro(HttpStatus.BAD_REQUEST, message);
    }

    // Handler geral para violação de regra de negócios (erro 400)
    @ExceptionHandler(OperacaoInvalidaException.class)
    public ResponseEntity<RestErrorMessage> handleBadRequest(OperacaoInvalidaException ex) {
        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Handler para erro 500
    @ExceptionHandler(ErroGeracaoTokenExcecao.class)
    public ResponseEntity<RestErrorMessage> handleInternalServerError(ErroGeracaoTokenExcecao ex) {
      
        return criarRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor. Tente novamente mais tarde.");
    }
    
    // Método auxiliar para criar a resposta padronizada
    private ResponseEntity<RestErrorMessage> criarRespostaErro(HttpStatus status, String message) {
        RestErrorMessage mensagemErro = new RestErrorMessage(status, message);
        return ResponseEntity.status(status).body(mensagemErro);
    }
}