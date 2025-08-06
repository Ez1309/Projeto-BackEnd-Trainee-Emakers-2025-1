package com.biblioteca.api_biblioteca.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.repository.PessoaRepository;
import com.biblioteca.api_biblioteca.service.security.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    PessoaRepository pessoaRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            var email = tokenService.validarToken(token);
            if (email != null) {
                Optional<Pessoa> pessoaOptional = pessoaRepository.findByEmail(email);

                if (pessoaOptional.isPresent()) {
                    UserDetails pessoa = pessoaOptional.get();
                    var authentication = new UsernamePasswordAuthenticationToken(pessoa, null, pessoa.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }

            }

        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
