package br.com.luiabdiel.ms_audit_v1.core.domain.port.out;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditPortOut {

    AuditCustomerEntity save(AuditCustomerEntity auditCustomerEntity);

    Page<AuditCustomerEntity> findAll(Pageable pageable);
}
