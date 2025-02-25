package br.com.luiabdiel.ms_audit_v1.infrastructure.persistence.repository;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditCustomerEntity, Long> {
}
