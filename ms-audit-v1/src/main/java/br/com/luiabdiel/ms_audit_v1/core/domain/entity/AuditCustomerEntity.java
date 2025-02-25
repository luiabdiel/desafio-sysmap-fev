package br.com.luiabdiel.ms_audit_v1.core.domain.entity;

import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_AUDIT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuditCustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, name = "customer_id")
    private Long customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public AuditCustomerEntity(Long customerId, String name, String email, EventType eventType) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.eventType = eventType;
    }
}
