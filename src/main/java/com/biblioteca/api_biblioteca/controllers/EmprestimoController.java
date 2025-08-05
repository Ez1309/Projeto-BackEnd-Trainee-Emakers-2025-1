package com.biblioteca.api_biblioteca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.biblioteca.api_biblioteca.data.dto.request.EmprestimoRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.EmprestimoResponseDTO;
import com.biblioteca.api_biblioteca.data.dto.response.EmprestimoUsuarioResponseDTO;
import com.biblioteca.api_biblioteca.data.dto.response.PessoaResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.exceptions.RestErrorMessage;
import com.biblioteca.api_biblioteca.service.EmprestimoService;

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
import jakarta.validation.valueextraction.ExtractedValue;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos", description = "Endpoints para gerenciamento de empréstimos.")
public class EmprestimoController {
    
    @Autowired
    private EmprestimoService emprestimoService;

    @Operation(
        summary = "Lista com o registro de todos os empréstimos (ADMIN)",
        description = "Retorna uma lista com o registro de todos os empréstimos (ativos e devolvidos). Requer permissão de ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sucesso - Lista de empréstimos retornada",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EmprestimoResponseDTO.class)),
                examples = @ExampleObject(
                    value = """
                            [
                                {
                                    "id": 1,
                                    "pessoaResponseDTO": {
                                        "id": 2,
                                        "name": "Regina Júlia Almada",
                                        "cpf": "93958818609",
                                        "cep": "81900749",
                                        "rua": "Rua Cláudio Alves de Lima",
                                        "bairro": "Sítio Cercado",
                                        "cidade": "Curitiba",
                                        "estado": "PR",
                                        "email": "reginajul_almada@gmail.com",
                                        "papel": "USER"
                                    },
                                    "livroResponseDTO": {
                                        "id": 5,
                                        "name": "Fundação",
                                        "autor": "Isaac Asimov",
                                        "dataLancamento": "01/06/1951",
                                        "disponivel": true
                                    },
                                    "dataEmprestimo": "2025-08-05",
                                    "dataDevolucaoAgendada": "2025-08-15",
                                    "dataDevolucaoReal": "2025-08-10",
                                    "status": "DEVOLVIDO"
                                }
                            ]
                            """
                )
            )

        ),
        @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @GetMapping(value = "/all")
    public ResponseEntity<List<EmprestimoResponseDTO>> getAllEmprestimos(){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getAllEmprestimos());
    }


    @Operation(
        summary = "Busca um empréstimo por ID",
        description = "Retorna informações de um empréstimo específico com base no seu ID. Requer autenticação"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sucesso - Empréstimo encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmprestimoResponseDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "id": 1,
                        "pessoaResponseDTO": {
                            "id": 2,
                            "name": "Regina Júlia Almada",
                            "cpf": "93958818609",
                            "cep": "81900749",
                            "rua": "Rua Cláudio Alves de Lima",
                            "bairro": "Sítio Cercado",
                            "cidade": "Curitiba",
                            "estado": "PR",
                            "email": "reginajul_almada@gmail.com",
                            "papel": "USER"
                        },
                        "livroResponseDTO": {
                            "id": 5,
                            "name": "Fundação",
                            "autor": "Isaac Asimov",
                            "dataLancamento": "01/06/1951",
                            "disponivel": true
                        },
                        "dataEmprestimo": "2025-08-05",
                        "dataDevolucaoAgendada": "2025-08-15",
                        "dataDevolucaoReal": "2025-08-10",
                        "status": "DEVOLVIDO"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Não Encontrado - O ID do empréstimo informado não existe",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "04-08-2025 19:45:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o id: 1"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer autenticação", content = @Content)
    })
    @GetMapping(value = "/{idEmprestimo}/find")
    public ResponseEntity<EmprestimoResponseDTO> getEmprestimoById(
        @Parameter(description = "ID do empréstimo a ser buscado.", example = "1", required = true)
        @PathVariable Long idEmprestimo){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.getEmprestimoById(idEmprestimo));
    }


    @Operation(
        summary = "Cria um novo empréstimo (USER)",
        description = "Registra um novo empréstimo de um livro para o usuário autenticado. Requer permissão de USER.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para o novo empréstimo.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmprestimoRequestDTO.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "idLivro": 4,
                        "dataDevolucaoAgendada": "25/08/2025"
                    }
                    """
                )
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Sucesso - Empréstimo realizado",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = EmprestimoUsuarioResponseDTO.class),
                examples = @ExampleObject(
                    value = """
                            {
                                "idEmprestimo": 3,
                                "nomeLivro": "Duna",
                                "autor":
                                "dataEmprestimo": "05/08/2025",
                                "dataDevolucaoAgendada": "15/08/2025"
                                "dataDevolucaoReal":null,
                                "status":"ATIVO"
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição Inválida - Dados inválidos",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = RestErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                            {
                                "timestamp": "05-08-2025 19:26:16",
                                "status": 400,
                                "error": "Bad Request",
                                "message": "O campo 'dataDevolucaoAgendada' contém uma data no formato inválido. Use o formado dd/MM/yyyy."
                            },
                            {
                                "timestamp": "05-08-2025 19:27:03",
                                "status": 400,
                                "error": "Bad Request",
                                "message": "O período máximo de empréstimo é de 180 dias (6 meses)"
                            },
                            {
                                "timestamp": "05-08-2025 19:27:21",
                                "status": 400,
                                "error": "Bad Request",
                                "message": "A data de devolução deve ser uma data futura."
                            }

                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Não Encontrado - O livro com o ID informado não existe",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = RestErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                            {
                                "timestamp": "05-08-2025 19:28:50",
                                "status": 404,
                                "error": "Not Found",
                                "message": "Não foi encontrada a entidade com o ID: 1"
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflito - O livro solicitado não está disponível para empréstimo",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = RestErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                            {
                                "timestamp": "05-08-2025 19:29:46",
                                "status": 409,
                                "error": "Conflict",
                                "message": "O livro 'Duna' não está disponível para empréstimo"
                            }
                            """
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de USER", content = @Content)
    })
    @PostMapping(value = "/create")
    public ResponseEntity<EmprestimoUsuarioResponseDTO> criarEmprestimo(@RequestBody @Valid EmprestimoRequestDTO emprestimoRequestDTO, @AuthenticationPrincipal Pessoa pessoaLogada){
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.criarEmprestimo(emprestimoRequestDTO, pessoaLogada));
    }


    @Operation(
        summary = "Lista os empréstimos do usuário logado (USER)",
        description = "Retorna uma lista com todos os empréstimos (ativos e devolvidos) pertencentes ao usuário autenticado. Requer permissão de USER."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sucesso - Lista de empréstimos retornada",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EmprestimoUsuarioResponseDTO.class)),
                examples = @ExampleObject(
                    value = """
                            [
                                {
                                    "idEmprestimo": 1,
                                    "nomeLivro": "Fundação",
                                    "autorLivro": "Isaac Asimov",
                                    "dataEmprestimo": "05/08/2025",
                                    "dataDevolucaoAgendada": "15/08/2025",
                                    "dataDevolucaoReal": "05/08/2025",
                                    "status": "DEVOLVIDO"
                                },
                                {
                                    "idEmprestimo": 3,
                                    "nomeLivro": "Duna",
                                    "autorLivro": "Frank Herbert",
                                    "dataEmprestimo": "05/08/2025",
                                    "dataDevolucaoAgendada": "15/08/2025",
                                    "dataDevolucaoReal": null,
                                    "status": "ATIVO"
                                }
                            ]
                            """
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de USER", content = @Content)
    })
    @GetMapping(value = "/meus-emprestimos")
    public ResponseEntity<List<EmprestimoUsuarioResponseDTO>> getMeusEmprestimos(@AuthenticationPrincipal Pessoa pessoaLogada){
        List<EmprestimoUsuarioResponseDTO> emprestimos = emprestimoService.getMeusEmprestimos(pessoaLogada);
        return ResponseEntity.ok(emprestimos);
    }


    @Operation(
        summary = "Realiza a devolução de um livro (USER)",
        description = "Marca um empréstimo ativo como 'DEVOLVIDO' e torna o livro associado disponível novamente. Requer permissão de USER."
    )
    @ApiResponses(value = {
        @ApiResponse(
        responseCode = "200",
        description = "Sucesso - Devolução realizada",
        content = @Content(
            mediaType = "application/json", 
            schema = @Schema(implementation = EmprestimoUsuarioResponseDTO.class),
            examples = @ExampleObject(
                value = """
                    {
                        "idEmprestimo": 1,
                        "nomeLivro": "Fundação",
                        "autorLivro": "Isaac Asimov",
                        "dataEmprestimo": "05/08/2025",
                        "dataDevolucaoAgendada": "15/08/2025",
                        "dataDevolucaoReal": "10/08/2025",
                        "status": "DEVOLVIDO"
                    }
                        """
            )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Requisição Inválida - O empréstimo já foi finalizado ou o usuário não é o dono do empréstimo",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = RestErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                            {
                                "timestamp": "05-08-2025 19:50:46",
                                "status": 400,
                                "error": "Bad Request",
                                "message": "Esse empréstimo já foi finalizado."
                            },
                            {
                                "timestamp": "05-08-2025 20:17:28",
                                "status": 400,
                                "error": "Bad Request",
                                "message": "Você não tem permissão para devolver um empréstimo que não é seu."
                            }

                            """
                )
                )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Não Encontrado - O empréstimo com o ID informado não existe",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = RestErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                        {
                            "timestamp": "04-08-2025 16:55:00",
                            "status": 404,
                            "error": "Not Found",
                            "message": "Não foi encontrada a entidade com o ID: 1"
                        }
                            """
                )
                )
        ),
        @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de USER", content = @Content)
    })
    @PatchMapping(value = "/{idEmprestimo}/devolucao")
    public ResponseEntity<EmprestimoUsuarioResponseDTO> realizarDevolucao(@PathVariable Long idEmprestimo, @AuthenticationPrincipal Pessoa pessoaLogada){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.realizarDevolucao(idEmprestimo, pessoaLogada));
    }
}
