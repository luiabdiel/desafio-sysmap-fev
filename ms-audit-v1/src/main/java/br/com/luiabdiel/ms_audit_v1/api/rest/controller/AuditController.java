package br.com.luiabdiel.ms_audit_v1.api.rest.controller;

import br.com.luiabdiel.ms_audit_v1.core.domain.port.out.dto.AuditResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Audits", description = "Gerencia os registros de auditoria do sistema")
public interface AuditController {

    @Operation(
            summary = "Busca todas as auditorias",
            description = "Retorna uma lista paginada contendo todas as auditorias registradas no sistema. " +
                    "Os resultados podem ser filtrados e ordenados através dos parâmetros de paginação."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AuditResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor ao processar a solicitação.",
                            content = @Content
                    )
            }
    )
    @GetMapping(value = "/audits")
    ResponseEntity<Page<AuditResponseDto>> findAll(Pageable pageable);
}
