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

import com.biblioteca.api_biblioteca.data.dto.request.AuthenticationDTO;
import com.biblioteca.api_biblioteca.data.dto.request.RegisterDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PessoaRepository pessoaRepository;
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody @Valid AuthenticationDTO autenticacao){
        var senhaPessoa = new UsernamePasswordAuthenticationToken(autenticacao.email(), autenticacao.senha());
        var auth = this.authenticationManager.authenticate(senhaPessoa);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<RegisterDTO> registrar(@RequestBody @Valid RegisterDTO data){
        if (this.pessoaRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();
        
        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());

        Pessoa novaPessoa = new Pessoa(data.nome(), data.cpf(), data.cep(), data.email(), senhaCriptografada, data.role());

        this. pessoaRepository.save(novaPessoa);

        return ResponseEntity.ok().build();
    }
}
