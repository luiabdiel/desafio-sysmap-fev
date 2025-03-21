package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.UserRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Autenticação do sistema")
public interface AuthController {

    @Operation(
            summary = "Registra um novo usuário",
            description = "Cria um novo usuário no sistema com os dados fornecidos. " +
                    "Caso o e-mail já esteja cadastrado, a operação será rejeitada."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário registrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Usuário já cadastrado no sistema.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @PostMapping(value = "/auth/register")
    ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto);

    @Operation(
            summary = "Realiza login do usuário",
            description = "Autentica um usuário com base no e-mail e senha fornecidos. " +
                    "Retorna um token JWT caso a autenticação seja bem-sucedida."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciais inválidas",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @PostMapping(value = "/auth/login")
    ResponseEntity<String> login(@RequestBody UserRequestDto userRequestDto);
}
