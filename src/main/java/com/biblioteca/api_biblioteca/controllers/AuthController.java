package com.biblioteca.api_biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.api_biblioteca.data.dto.request.AlterarSenhaRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.request.AuthRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.request.RegisterRequestDTO;
import com.biblioteca.api_biblioteca.data.dto.response.LoginResponseDTO;
import com.biblioteca.api_biblioteca.data.entity.Pessoa;
import com.biblioteca.api_biblioteca.exceptions.RestErrorMessage;
import com.biblioteca.api_biblioteca.service.security.AuthService;
import com.biblioteca.api_biblioteca.service.security.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para registro, login e alteração de senha.")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Realiza o login de um usuário", description = "Autentica um usuário com email e senha e retora um token JWT para ser usado nas requisições", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para autenticação.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthRequestDTO.class), examples = @ExampleObject(value = """
            {
                "email": "joao_da_silva@gmail.com",
                "senha": "joao123"
            }
            """))

    ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso - Login realizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class), examples = @ExampleObject(value = """
                    {
                     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGktYmlibGlvdGVjYSIsInN1YiI6InVzZXIiLCJleHAiOjE3NTQ0OTMyODd9.GlG8Qk4Nq5MoO7fEYAkYEhuOzdVdFoMNT28zk64CVN"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida - Dados inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "06-08-2025 10:33:41",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "O email é obrigatório"
                        },
                        {
                            "timestamp": "06-08-2025 10:33:41",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "A senha é obrigatória"
                        }

                    ]
                    """))),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Credenciais inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                        {
                            "timestamp": "06-08-2025 10:33:41",
                            "status": 401,
                            "error": "Unauthorized",
                            "message": "Usuário inexistente ou senha inválida"
                        }
                    """))),

    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthRequestDTO autenticacao) {
        var senhaPessoa = new UsernamePasswordAuthenticationToken(autenticacao.email(), autenticacao.senha());
        var auth = this.authenticationManager.authenticate(senhaPessoa);

        var token = tokenService.gerarToken((Pessoa) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Registra um novo usuário", description = "Cria um novo usuário com o perfil 'USER'. O endereço é preenchido automaticamente via API do ViaCEP.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a ser registrado", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterRequestDTO.class), examples = @ExampleObject(value = """
            {
                "nome": "Regina Júlia Almada",
                "cpf": "93958818609",
                "cep": "81900749",
                "email": "reginajul_almada@gmail.com",
                "senha": "juliaAlmada1998*@"
            }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso - Pessoa registrada", content = @Content()),
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
            @ApiResponse(responseCode = "409", description = "Conflito - Uma pessoa com o mesmo email ou senha já está cadastrada", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
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
                    """)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegisterRequestDTO registerDTO) {

        authService.registrar(registerDTO);
        return ResponseEntity.status((HttpStatus.CREATED)).build();
    }

    @Operation(summary = "Altera a senha do usuário registrado", description = "Permite que o usuário autenticado altere sua própria senha, fornecendo a senha atual e a nova senha.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlterarSenhaRequestDTO.class), examples = @ExampleObject(value = """
            {
                "senhaAtual": "joao123",
                "novaSenha": "joao723@*&"
            }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucesso - Senha alterada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida - Senha atual incorreta ou dados inválidos", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestErrorMessage.class)), examples = @ExampleObject(value = """
                    [
                        {
                            "timestamp": "06-08-2025 13:31:39",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "A nova senha é obrigatória"
                        },
                        {
                            "timestamp": "06-08-2025 13:31:39",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "A senha atual é obrigatória"
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "401", description = "Não autorizado - A senha atual digitada está incorreta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestErrorMessage.class), examples = @ExampleObject(value = """
                    {
                        "timestamp": "06-08-2025 13:27:08",
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "A senha atual informada está incorreta."
                    }
                    """))),
            @ApiResponse(responseCode = "403", description = "Não Autorizado - Token ausente ou inválido", content = @Content)
    })
    @PostMapping("/me/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid AlterarSenhaRequestDTO alterarSenhaRequestDTO,
            @AuthenticationPrincipal Pessoa pessoaLogada) {
        authService.alterarSenha(alterarSenhaRequestDTO, pessoaLogada);
        return ResponseEntity.noContent().build();
    }
}
