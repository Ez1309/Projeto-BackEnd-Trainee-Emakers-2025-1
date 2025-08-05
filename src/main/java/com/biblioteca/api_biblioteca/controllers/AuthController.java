package com.biblioteca.api_biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.api_biblioteca.data.dto.request.AlterarSenhaRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.request.AuthRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.request.RegisterRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.LoginResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.service.security.AuthService;
import com.biblioteca.api_biblioteca.service.security.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthRequestDTO autenticacao){
        var senhaPessoa = new UsernamePasswordAuthenticationToken(autenticacao.email(), autenticacao.senha());
        var auth = this.authenticationManager.authenticate(senhaPessoa);

        var token = tokenService.gerarToken((Pessoa) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegisterRequestDTO registerDTO){
      
        authService.registrar(registerDTO);
        return ResponseEntity.status((HttpStatus.CREATED)).build();
    }

    @PostMapping("/me/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid AlterarSenhaRequestDTO alterarSenhaRequestDTO, @AuthenticationPrincipal Pessoa pessoaLogada){
        authService.alterarSenha(alterarSenhaRequestDTO, pessoaLogada);
        return ResponseEntity.noContent().build();
    }
}
