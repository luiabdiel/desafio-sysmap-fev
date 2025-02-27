package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Customers", description = "Gerencia os clientes do sistema")
public interface CustomerController {

    @Operation(
            summary = "Cria um novo cliente",
            description = "Cria um novo cliente no sistema com os dados fornecidos. " +
                    "Caso o e-mail já esteja cadastrado, a operação será rejeitada."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cliente criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "E-mail já cadastrado no sistema.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @PostMapping(value = "/customers")
    ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerRequestDto customerRequestDto);

    @Operation(
            summary = "Busca um cliente por ID",
            description = "Retorna os detalhes de um cliente com base no ID fornecido. Caso o cliente não seja encontrado, retorna um erro."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @GetMapping(value = "/customers/{id}")
    ResponseEntity<CustomerResponseDto> findById(@PathVariable(value = "id") Long id);

    @Operation(
            summary = "Busca todos os clientes com paginação",
            description = "Retorna uma lista paginada de clientes cadastrados no sistema."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de clientes recuperada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @GetMapping(value = "/customers")
    ResponseEntity<Page<CustomerResponseDto>> findAll(Pageable pageable);

    @Operation(
            summary = "Atualiza um cliente",
            description = "Atualiza as informações de um cliente existente com base no ID fornecido. " +
                    "Caso o cliente não seja encontrado, retorna um erro."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "E-mail já cadastrado no sistema.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @PutMapping(value = "/customers/{id}")
    ResponseEntity<CustomerResponseDto> update (
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid CustomerRequestDto customerRequestDto);

    @Operation(
            summary = "Exclui um cliente",
            description = "Exclui um cliente existente com base no ID fornecido. Caso o cliente não seja encontrado, retorna um erro."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Cliente excluído com sucesso",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping(value = "/customers/{id}")
    ResponseEntity<CustomerResponseDto> deleteById(@PathVariable(value = "id") Long id);
}
