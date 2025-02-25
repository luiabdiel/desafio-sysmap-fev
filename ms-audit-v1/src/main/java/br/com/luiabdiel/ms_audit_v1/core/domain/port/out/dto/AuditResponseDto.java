package br.com.luiabdiel.ms_audit_v1.core.domain.port.out.dto;

import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditResponseDto {

    private Long id;

    private Long customerId;

    private String name;

    private String email;

    private EventType eventType;
}
