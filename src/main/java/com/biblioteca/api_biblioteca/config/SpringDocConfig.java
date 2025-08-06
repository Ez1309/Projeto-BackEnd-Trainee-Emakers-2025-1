package com.biblioteca.api_biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o seu token JWT para autenticar as requisições.")))

                .info(new Info()
                        .title("API Biblioteca")
                        .description(
                                "API REST para gerenciamento de uma biblioteca, com controle de pessoas, livros e empréstimos. \n\n\n Feito por: Ezequiel Dominguez Santos - Trainee Emakers Jr. PS 2025/1")
                );
    }
}