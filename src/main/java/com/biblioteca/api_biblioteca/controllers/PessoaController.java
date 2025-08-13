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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.api_biblioteca.data.dto.request.PessoaAdminUpdateDTO;
import com.biblioteca.api_biblioteca.data.dto.request.PessoaRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.PessoaResponseDTO;
import com.biblioteca.api_biblioteca.exceptions.RestErrorMessage;
import com.biblioteca.api_biblioteca.service.PessoaService;

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
@RequestMapping("/pessoas")
@Tag(name = "Pessoas", description = "Endpoints para gerenciamento de Pessoas (Acesso exclusivo de ADMIN)")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Operation(summary = "Lista todas as pessoas cadastradas (ADMIN)", description = "Retorna uma lista com todas as pessoas cadastradas no sistema. Requer permissão de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Lista de pessoas retornada", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PessoaResponseDTO.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "nome": "Gustavo Manoel Ramos",
                            "cpf": "06211934025",
                            "cep": "79012140",
                            "rua": "Rua Senador Feijó",
                            "bairro": "Coronel Antonino",
                            "cidade": "Campo Grande",
                            "estado": "MS",
                            "email": "gustavo_ramos@outlook.com",
                            "papel": "ADMIN"
                        },
                        {
                            "id": 2,
                            "nome": "Regina Júlia Almada",
                            "cpf": "93958818609",
                            "cep": "81900749",
                            "rua": "Rua Cláudio Alves de Lima",
                            "bairro": "Sítio Cercado",
                            "cidade": "Curitiba",
                            "estado": "PR",
                            "email": "reginajul_almada@gmail.com",
                            "papel": "USER"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<List<PessoaResponseDTO>> getAllPessoas() {
        return ResponseEntity.ok(pessoaService.getAllPessoas());
    }

    @Operation(summary = "Busca uma pessoa por ID (ADMIN)", description = "Retorna informações de uma pessoa específica com base no seu ID. Requer permissão de ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Pessoa encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 2,
                        "nome": "Regina Júlia Almada",
                        "cpf": "93958818609",
                        "cep": "81900749",
                        "rua": "Rua Cláudio Alves de Lima",
                        "bairro": "Sítio Cercado",
                        "cidade": "Curitiba",
                        "estado": "PR",
                        "email": "reginajul_almada@gmail.com",
                        "papel": "USER"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Não Encontrado - O ID da pessoa informada não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:45:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o id: 1"
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @GetMapping("/{idPessoa}/find")
    public ResponseEntity<PessoaResponseDTO> getPessoaById(
            @Parameter(description = "ID da pessoa a ser buscada.", example = "1", required = true) @PathVariable Long idPessoa) {
        return ResponseEntity.ok(pessoaService.getPessoaById(idPessoa));
    }

    @Operation(summary = "Cria uma nova pessoa (ADMIN)", description = "Administrador cadastra um novo usuário no sistema. A senha é definida como nula; o usuário precisará usar um fluxo de recuperação de senha para definir uma antes do primeiro login. Requer permissão de ADMIN.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da pessoa a ser criada, sem o campo senha.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaRequestDTO.class), examples = @ExampleObject(value = """
            {
                "nome": "Regina Júlia Almada",
                "cpf": "93958818609",
                "cep": "81900749",
                "email": "reginajul_almada@gmail.com",
                "papel": "USER"
            }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso - Pessoa criada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 2,
                        "nome": "Regina Júlia Almada",
                        "cpf": "93958818609",
                        "cep": "81900749",
                        "rua": "Rua Cláudio Alves de Lima",
                        "bairro": "Sítio Cercado",
                        "cidade": "Curitiba",
                        "estado": "PR",
                        "email": "reginajul_almada@gmail.com",
                        "papel": "USER"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida - Dados inválidos", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "05-08-2025 18:10:15",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "CEP é obrigatório"
                        },
                        {
                            "timestamp": "05-08-2025 18:10:15",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Email é obrigatório"
                        },
                        {
                            "timestamp": "05-08-2025 18:10:15",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Nome é obrigatório"
                        },
                        {
                            "timestamp": "05-08-2025 18:03:35",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "O CPF deve conter 11 dígitos, sem pontos ou traços"
                        },
                        {
                            "timestamp": "05-08-2025 18:03:35",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "CPF Inválido"
                     }
                    ]
                    """))),
            @ApiResponse(responseCode = "409", description = "Conflito - Uma pessoa com estes mesmos dados já existe", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "06-08-2025 10:40:00",
                            "status": 409,
                            "error": "Conflict",
                            "message": "O email informado já está em uso."
                        },
                        {
                            "timestamp": "06-08-2025 10:40:31",
                            "status": 409,
                            "error": "Conflict",
                            "message": "O CPF informado já está cadastrado"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<PessoaResponseDTO> criarPessoa(@Valid @RequestBody PessoaRequestDTO pessoaRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.criarPessoa(pessoaRequestDTO));
    }

    @Operation(summary = "Atualiza uma pessoa existente (ADMIN)", description = "Atualiza os dados de uma com base no seu ID. Apenas os campos no corpo da requisição são alterados. Requer permissão de ADMIN.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novos dados para a pessoa. O campo senha não é permitido para essa operação", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaAdminUpdateDTO.class), examples = @ExampleObject(value = """
            {
                "nome": "Regina Júlia Almada",
                "cpf": "93958818609",
                "cep": "81900749",
                "email": "reginajul_almada@gmail.com",
                "papel": "USER"
            }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Pessoa atualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "nome": "Regina Júlia Almada",
                        "cpf": "93958818609",
                        "cep": "81900749",
                        "rua": "Rua Cláudio Alves de Lima",
                        "bairro": "Sítio Cercado",
                        "cidade": "Curitiba",
                        "estado": "PR",
                        "email": "reginajul_almada@gmail.com",
                        "papel": "USER"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida - Dados do corpo da requisição são inválidos", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "05-08-2025 18:03:35",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "O CPF deve conter 11 dígitos, sem pontos ou traços"
                        },
                        {
                            "timestamp": "05-08-2025 18:03:35",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "CPF Inválido"
                        }
                    ]
                        """))),
            @ApiResponse(responseCode = "404", description = "Não Encontrado - O ID da pessoa informada não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:31:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o id: 1"
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "Conflito - Já existe outra pessoa com os mesmos dados informados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:32:00",
                        "status": 409,
                        "error": "Conflict",
                        "message": "Já existe outra pessoa cadastrada com estes mesmos dados."
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })

    @PatchMapping("/{idPessoa}/update")
    public ResponseEntity<PessoaResponseDTO> atualizarPessoa(
            @Parameter(description = "ID da pessoa a ser atualizada.", example = "1", required = true) @PathVariable Long idPessoa,
            @Valid @RequestBody PessoaAdminUpdateDTO pessoaAdminUpdateDTO) {
        return ResponseEntity.ok(pessoaService.atualizarPessoaAdmin(idPessoa, pessoaAdminUpdateDTO));
    }

    @Operation(summary = "Deleta uma pessoa por ID (ADMIN)", description = "Remove uma pessoa do banco de dados com base no seu ID. Requer permissão de ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucesso - Pessoa deletada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Não Encontrado - O ID da pessoa informada não existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "04-08-2025 19:45:00",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Não foi encontrada a entidade com o id: 1"
                    }
                    """))),

            @ApiResponse(responseCode = "403", description = "Não Autorizado - Requer perfil de ADMIN", content = @Content)
    })
    @DeleteMapping("/{idPessoa}/delete")
    public ResponseEntity<Void> deletarPessoa(
            @Parameter(description = "ID da pessoa a ser deletada.", example = "1", required = true) @PathVariable Long idPessoa) {
        pessoaService.deletarPessoa(idPessoa);
        return ResponseEntity.noContent().build();
    }
}