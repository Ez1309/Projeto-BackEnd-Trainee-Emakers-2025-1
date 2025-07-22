package com.biblioteca.api_biblioteca.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite todas as requisições
            )
            .httpBasic(b -> b.disable()) // Desabilita autenticação básica
            .formLogin(f -> f.disable()); // Desabilita login via formulário

        return http.build();
    }
}