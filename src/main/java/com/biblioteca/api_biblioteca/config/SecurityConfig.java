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
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/registrar").permitAll()
            

                // Crud pessoa só pode ser feito por administrador
                .requestMatchers("/pessoa/**").hasRole("ADMIN")

                // Só admin pode criar, atualizar e deletar um livro
                .requestMatchers(HttpMethod.POST, "/livro/create").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/livro/update/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/livro/delete/{id}").hasRole("ADMIN")

                // Resto das rotas liberadas para quem estiver credenciado
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