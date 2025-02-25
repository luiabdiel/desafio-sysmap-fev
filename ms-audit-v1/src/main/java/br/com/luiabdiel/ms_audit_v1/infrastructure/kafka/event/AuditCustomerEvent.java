package br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuditCustomerEvent {

    private Long id;

    private String name;

    private String email;

    private EventType eventType;

    public AuditCustomerEvent(String name, String email, EventType eventType) {
        this.name = name;
        this.email = email;
        this.eventType = eventType;
    }
}
