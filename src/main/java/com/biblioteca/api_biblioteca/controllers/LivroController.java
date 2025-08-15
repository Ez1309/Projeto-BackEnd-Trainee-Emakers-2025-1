package com.biblioteca.api_biblioteca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.api_biblioteca.data.dto.request.LivroRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.LivroResponseDTO;
import com.biblioteca.api_biblioteca.data.dto.request.AtualizarEstoqueRequestDTO;
import com.biblioteca.api_biblioteca.exceptions.RestErrorMessage;
import com.biblioteca.api_biblioteca.service.LivroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Endpoints para gerenciamento de Livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @Operation(summary = "Busca todos os livros cadastrados", description = "Retorna uma lista com todos os livros cadastrados no banco de dados", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Lista encontrada", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LivroResponseDTO.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "nome": "O Senhor dos Anéis",
                            "autor": "J.R.R. Tolkien",
                            "dataLancamento": "29/07/1954",
                            "quantidadeTotal": 5,
                            "quantidadeDisponvivel": 3
                        },
                        {
                            "id": 2,
                            "nome": "O Guia do Mochileiro das Galáxias",
                            "autor": "Douglas Adams",
                            "dataLancamento": "12/10/1979",
                            "quantidadeTotal": 1,
                            "quantidadeDisponvivel": 0
                        }
                    ]
                            """))),
            @ApiResponse(responseCode = "403", description = "Não autorizado / Token Inválido", content = @Content)
    })

    @GetMapping(value = "/all")
    public ResponseEntity<List<LivroResponseDTO>> getAllLivros() {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getAllLivros());
    }

    @Operation(summary = "Busca um livro por ID", description = "Retorna informações de um livro específico com base no seu ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Livro encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nome": "O Senhor dos Anéis",
                        "autor": "J.R.R. Tolkien",
                        "dataLancamento": "29/07/1954",
                        "quantidadeTotal": 4,
                        "quantidadeDisponvel": 3
                    }
                            """)

            )),
            @ApiResponse(responseCode = "404", description = "Não encontrado - O ID do livro não existe no banco de dados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 16:55:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o ID: 1"
                    }
                            """)

            )),
            @ApiResponse(responseCode = "403", description = "Não autorizado - O usuário não está autenticado", content = @Content),
    })
    @GetMapping(value = "/{idLivro}/find")
    public ResponseEntity<LivroResponseDTO> getLivroById(
            @Parameter(description = "ID do livro a ser buscado.", example = "1", required = true) @PathVariable Long idLivro) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getLivroById(idLivro));
    }

    @Operation(summary = "Cria um novo livro (ADMIN)", description = "Cadastra um novo livro no sistema. Requer permissão de ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do livro a ser criado.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroRequestDTO.class), examples = @ExampleObject(value = """
                    {
                        "nome": "O Hobbit",
                        "autor": "J.R.R. Tolkien",
                        "dataLancamento": "21/09/1937",
                        "quantidade": 3
                    }
                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso - Livro criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 6,
                        "nome": "O Hobbit",
                        "autor": "J.R.R. Tolkien",
                        "dataLancamento": "21/09/1937",
                        "quantidadeTotal": 4,
                        "quantidadeDisponivel": 3
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida - Um ou mais campos do corpo da requisição são inválidos", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "04-08-2025 18:10:00",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Nome é obrigatório"
                        },
                        {
                            "timestamp": "04-08-2025 18:10:00",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Autor é obrigatório"
                        },
                        {
                            "timestamp": "04-08-2025 18:57:11",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "O campo 'dataLancamento' contém uma data no formato inválido. Use o formado dd/MM/yyyy."
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "409", description = "Conflito - Um livro com estes mesmos dados já existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "2025-08-2025 17:27:00",
                        "status": 409,
                        "error": "Conflict",
                        "message": "Um livro com essas características já existe"
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })

    @PostMapping(value = "/create")
    public ResponseEntity<LivroResponseDTO> criarLivro(@RequestBody @Valid LivroRequestDTO livroRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criarLivro(livroRequestDTO));
    }

    @Operation(
        summary = "Atualiza o estoque de um livro (ADMIN)",
        description = "Adiciona ou remove cópias de um livro existente. Número positivo adiciona e número negativo remove. Requer permissão de ADMIN.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Quantidade a ser adicionada ou removida ao estoque.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AtualizarEstoqueRequestDTO.class),
                examples = @ExampleObject(
                    value = """
                            {
                                "quantidade": 5
                            }
                            """
                )
            )

        )
    )
     @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso - Estoque atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição Inválida - A quantidade informada resulta em estoque negativo", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class))),
        @ApiResponse(responseCode = "404", description = "Não Encontrado - O livro com o ID informado não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class))),
        @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @PatchMapping("/{idLivro}/estoque")
    public ResponseEntity<LivroResponseDTO> atualizarEstoque(
        @Parameter(description = "ID do livro para ajustar o estoque.", required = true)
        @PathVariable Long idLivro, @Valid @RequestBody AtualizarEstoqueRequestDTO atualizarEstoqueRequestDTO){
            LivroResponseDTO livroAtualizado = livroService.atualizarEstoque(idLivro, atualizarEstoqueRequestDTO);
            return ResponseEntity.ok(livroAtualizado);
        }
    
    @Operation(summary = "Atualiza um livro existente (ADMIN)", description = "Atualiza os dados de um livro com base no seu ID. Requer permissão de ADMIN.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novos dados para o livro. Todos os campos são obrigatórios.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroRequestDTO.class), examples = @ExampleObject(value = """
            {
                "nome": "O Hobbit (Edição Ilustrada)",
                "autor": "J.R.R. Tolkien",
                "dataLancamento": "21/09/2012",
                "quantidade": 3
            }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Livro atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 5,
                        "nome": "O Hobbit (Edição Ilustrada)",
                        "autor": "J.R.R. Tolkien",
                        "dataLancamento": "21/09/2012",
                        "quantidadeTotal": 4,
                        "quantidadeDisponivel" 3
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida - Dados do corpo da requisição são inválidos", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "04-08-2025 19:30:00",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Nome é obrigatório"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Não Encontrado - O ID do livro informado não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:31:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o id: 1"
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "Conflito - Já existe outro livro com os mesmos dados informados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:32:00",
                        "status": 409,
                        "error": "Conflict",
                        "message": "Já existe outro livro cadastrado com estes mesmos dados."
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })

    @PutMapping(value = "/{idLivro}/update")
    public ResponseEntity<LivroResponseDTO> atualizarLivro(
            @Parameter(description = "ID do livro a ser atualizado.", example = "1", required = true) @PathVariable Long idLivro,
            @RequestBody @Valid LivroRequestDTO livroRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.atualizarLivro(idLivro, livroRequestDTO));
    }

    @Operation(summary = "Deleta um livro por ID (ADMIN)", description = "Remove um livro do banco de dados com base no seu ID. Requer permissão de ADMIN. A operação falhará se o livro estiver atualmente emprestado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucesso - Livro deletado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Não Encontrado - O ID do livro informado não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:45:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o id: 1"
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "Conflito - Não é possível deletar um livro que está atualmente emprestado ou que já foi emprestado anteriormente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "04-08-2025 19:46:00",
                            "status": 409,
                            "error": "Conflict",
                            "message": "Não é possível deletar um livro que está atualmente emprestado."
                        },
                        {
                            "timestamp": "15-08-2025 18:42:01",
                            "status": 409,
                            "error": "Conflict",
                            "message": "Este livro não pode ser deletado pois já foi emprestado anteriormente."
                        }
                    ]        
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @DeleteMapping("/{idLivro}/delete")

    public ResponseEntity<Void> deletarLivro(
            @Parameter(description = "ID do livro a ser deletado.", example = "1", required = true) @PathVariable Long idLivro) {
        livroService.deletarLivro(idLivro);
        return ResponseEntity.noContent().build();
    }

}
