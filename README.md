# API de Gerenciamento de Biblioteca

> Status do Projeto: Concluído ✔️

## 📖 Sobre o Projeto

Esta é uma API RESTful para um sistema de gerenciamento de biblioteca, desenvolvida para a rota de backend no processo seletivo 2025/1 da Emakers Jr. O projeto foi construído com **Java 17 e Spring Boot 3**, com implementação de CRUD e boas práticas do mercado.

O sistema permite o gerenciamento completo de livros, usuários (pessoas) e o ciclo de vida de empréstimos, incluindo um sistema de autenticação baseado em JWT com controle de acesso por papéis (ADMIN/USER) e um tratamento de exceções detalhado.

---

## ✨ Funcionalidades Principais

O projeto inclui as seguintes funcionalidades:

* **Segurança com Spring Security e JWT:**
    * Autenticação stateless via Bearer Token.
    * Controle de acesso baseado em papéis (`ADMIN`, `USER`) para proteção de endpoints.
    * Endpoints para registro, login e alteração de senha pelo próprio usuário.
    * Criptografia de senhas utilizando BCrypt.

* **Gestão de Usuários (Pessoas):**
    * CRUD completo para administradores gerenciarem usuários.
    * Separação de responsabilidades: Admins podem criar usuários manualmente com senha padrão "123" e definir o papel; usuários podem se registrar digitando seus dados e têm o papel `USER`.
    * Atualização parcial (`PATCH`) de dados de usuários por administradores.

* **Gestão de Livros e Estoque:**
    * CRUD completo para administradores e usuários gerenciarem o catálogo de livros.
    * Sistema de controle de inventário com `quantidadeTotal` e `quantidadeDisponivel`.
    * Endpoint específico (`PATCH`) para administradores atualizarem o estoque de um livro.
    * Regras de negócio que impedem a criação/atualização de livros duplicados e a exclusão de títulos com exemplares emprestados.

* **Sistema de Empréstimos:**
    * Lógica completa para um usuário realizar e devolver um empréstimo.
    * Gestão de estado do empréstimo (`ATIVO`, `DEVOLVIDO`) e do livro (atualização automática da `quantidadeDisponivel`).
    * Validações de negócio (livro deve estar disponível, prazo máximo de empréstimo, etc.).
    * Endpoints para um usuário listar seus próprios empréstimos e para um admin listar todos.

* **Qualidade de API e Integrações:**
    * **Integração com API externa:** Consumo da API ViaCEP para preenchimento automático de endereço no cadastro.
    * **Tratamento de Exceções Personalizado:** Handler global (`@RestControllerAdvice`) que captura erros de negócio, segurança e do framework, retornando respostas JSON padronizadas e com os status HTTP corretos (`400`, `401`, `403`, `404`, `409`).
    * **Design RESTful:** Uso correto de verbos HTTP, status codes semânticos e DTOs (Data Transfer Objects) específicos para cada caso de uso.

---

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Security (JWT)**
* **Spring Data JPA / Hibernate**
* **PostgreSQL** (Banco de Dados)
* **Flyway** (Versionamento de Banco de Dados)
* **Lombok**
* **Spring Cloud OpenFeign** (para consumo da API ViaCEP)
* **SpringDoc OpenAPI (Swagger)** (para documentação)
* **Maven** (Gerenciador de Dependências)

---

## ⚙️ Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Maven 3.8+](https://maven.apache.org/download.cgi)
* [PostgreSQL](https://www.postgresql.org/download/) (ou uma instância rodando via Docker)

---

## 🚀 Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Ez1309/Projeto-BackEnd-Trainee-Emakers-2025-1
    ```

2.  **Configure as variáveis de ambiente:**
    Abra o arquivo `src/main/resources/application.properties` e configure as credenciais do seu banco de dados PostgreSQL.
    ```properties
    spring.datasource.url = jdbc:postgresql://localhost:5432/biblioteca-api
    spring.datasource.username = SEU_USUARIO_POSTGRES
    spring.datasource.password = SUA_SENHA_POSTGRES
    ```
    O segredo do JWT usará um valor padrão se a variável de ambiente `JWT_SECRET` não estiver definida. Para um ambiente de produção, é recomendado configurar esta variável.

3.  **Execute a aplicação:**
    Use o Maven para rodar o projeto. O Flyway criará e populará o banco de dados automaticamente na primeira inicialização.
    ```bash
    mvn spring-boot:run
    ```
    A API estará disponível em `http://localhost:8080`.

---

## 🧪 Como Testar a API

A forma mais fácil e recomendada de interagir com a API é através da documentação interativa do Swagger UI.

1.  Com a aplicação rodando, acesse a seguinte URL no seu navegador:
    **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

2.  **Para testar os endpoints protegidos:**
    * Use o endpoint `POST /auth/login` (com as credenciais padrão `user` ou `admin`) para obter um token JWT.
    * Clique no botão **"Authorize"** no canto superior direito.
    * Na janela que abrir, cole o token JWT no campo "Value" e clique em "Authorize".
    * Agora você pode explorar e executar todos os outros endpoints!

---

## ✒️ Autor

**Ezequiel Dominguez Santos**

* [LinkedIn](https://www.linkedin.com/in/ezequiel-dominguez-santos-54388330b/)
* [GitHub](https://github.com/Ez1309)