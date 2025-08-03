package com.biblioteca.api_biblioteca.service.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")

    private String secret;
    
    public String gerarToken(Pessoa pessoa){

        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            String token = JWT.create()
                .withIssuer("api-biblioteca")
                .withSubject(pessoa.getEmail())
                .withExpiresAt(generateExpirationDate())
                .sign(algoritmo);
            
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    public String validarToken(String token){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                .withIssuer("api-biblioteca")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println("\n\n\n\nToken inválido: " + exception.getMessage());
            System.out.println("Causa: " + exception.getCause());
            return "";
        }
    }

    public Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
