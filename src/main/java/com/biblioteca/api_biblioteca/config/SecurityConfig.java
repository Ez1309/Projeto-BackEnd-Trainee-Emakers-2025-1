package com.biblioteca.api_biblioteca.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            // ------------------- Endpoints Públicos -------------------
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/registrar").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

            // ------------------- Endpoints do Usuário Logado (Self-Service) -------------------
            .requestMatchers(HttpMethod.POST, "/auth/me/alterar-senha").hasRole("USER")
            

            // ------------------- Endpoints Gerais (USER & ADMIN) -------------------
            .requestMatchers(HttpMethod.POST, "/emprestimos/create").hasRole("USER")
            .requestMatchers(HttpMethod.PATCH, "/emprestimos/{idEmprestimo}/devolucao").hasRole("USER")
            // Admin vê a lista completa de empréstimos
            .requestMatchers(HttpMethod.GET, "/emprestimos/all").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/emprestimos/meus-emprestimos").hasRole("USER")
            
            .requestMatchers(HttpMethod.GET, "/emprestimos/{idEmprestimo}").authenticated() // Qualquer um logado pode ver um empréstimo específico

            // ------------------- Endpoints Exclusivos do ADMIN -------------------
            // Admin gerencia o ciclo de vida completo dos livros
            .requestMatchers(HttpMethod.POST, "/livros/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/livros/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/livros/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/livros/**").authenticated() // Qualquer um logado pode ver livros

            

            // Admin gerencia o ciclo de vida completo das pessoas
            .requestMatchers("/pessoas/**").hasRole("ADMIN")

            // Regra final: Qualquer outra requisição não listada acima precisa, no mínimo, de autenticação.
            // Esta regra foi efetivamente substituída pelas regras mais específicas acima, mas é uma boa
            // prática de segurança mantê-la como "pega-tudo" no final.
            .anyRequest().authenticated()
        )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}