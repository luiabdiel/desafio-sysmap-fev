package br.com.luiabdiel.ms_audit_v1.api.rest.controller;

import br.com.luiabdiel.ms_audit_v1.core.domain.port.in.AuditPortIn;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.out.dto.AuditResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuditControllerImpl implements AuditController {

    private final AuditPortIn auditPortIn;

    @Override
    public ResponseEntity<Page<AuditResponseDto>> findAll(@ParameterObject Pageable pageable) {
        log.info("[CONTROLLER - AuditControllerImpl.findAll] - Buscando todos as auditorias com paginação");
        Page<AuditResponseDto> auditsResponseDto = this.auditPortIn.findAll(pageable);

        log.info("[CONTROLLER - AuditControllerImpl.findAll] - Total de auditorias encontradas: {}", auditsResponseDto.getTotalElements());
        return ResponseEntity.ok().body(auditsResponseDto);
    }
}
