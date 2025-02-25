package br.com.luiabdiel.ms_audit_v1.api.rest.controller;

import br.com.luiabdiel.ms_audit_v1.core.domain.port.out.dto.AuditResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface AuditController {

    @GetMapping(value = "/audits")
    ResponseEntity<Page<AuditResponseDto>> findAll(Pageable pageable);
}
