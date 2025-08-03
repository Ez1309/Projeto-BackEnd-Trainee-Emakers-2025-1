package com.biblioteca.api_biblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            // Adiciona um item de segurança global, aplicando o "bearerAuth" a todos os endpoints
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            // Define o esquema de segurança que será usado
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Insira o seu token JWT para autenticar as requisições.")
                )
            )
            // Informações gerais da sua API
            .info(new Info()
                .title("API Biblioteca")
                //.version("v1.0")
                .description("API REST para gerenciamento de uma biblioteca, com controle de pessoas, livros e empréstimos. \n\n\n Feito por: Ezequiel Dominguez Santos - Trainee Emakers Jr. PS 2025/1")
                //.license(new License().name("Apache 2.0").url("http://springdoc.org"))
            );
    }
}