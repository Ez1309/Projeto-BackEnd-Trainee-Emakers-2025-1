package com.biblioteca.api_biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.api_biblioteca.config.TokenService;
import com.biblioteca.api_biblioteca.data.dto.request.AuthenticationDTO;
import com.biblioteca.api_biblioteca.data.dto.request.RegisterDTO;
import com.biblioteca.api_biblioteca.data.dto.response.LoginResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TokenService tokenService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO autenticacao){
        var senhaPessoa = new UsernamePasswordAuthenticationToken(autenticacao.email(), autenticacao.senha());
        var auth = this.authenticationManager.authenticate(senhaPessoa);

        var token = tokenService.gerarToken((Pessoa) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<RegisterDTO> registrar(@RequestBody @Valid RegisterDTO data){
        // Verificar essa parte pra throw exception de usuário já existente
        if (this.pessoaRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();
        
        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());

        Pessoa novaPessoa = new Pessoa(data.nome(), data.cpf(), data.cep(), data.email(), senhaCriptografada);

        this. pessoaRepository.save(novaPessoa);

        return ResponseEntity.ok().build();
    }
}
