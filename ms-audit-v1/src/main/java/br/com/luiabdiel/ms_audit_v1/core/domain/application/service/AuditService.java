package br.com.luiabdiel.ms_audit_v1.core.domain.application.service;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.in.AuditPortIn;
import br.com.luiabdiel.ms_audit_v1.infrastructure.persistence.AuditIntegrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService implements AuditPortIn {

    private final AuditIntegrator auditPortOut;

    public void save(AuditCustomerEntity auditCustomerEntity) {
        log.info("[PORT IN - AuditService.save] - Iniciando o salvamento do evento de auditoria para o cliente: ID={}, Name={}, Email={}",
                auditCustomerEntity.getId(), auditCustomerEntity.getName(), auditCustomerEntity.getEmail());

        try {
            this.auditPortOut.save(auditCustomerEntity);
            log.info("[PORT IN - AuditService.save] - Evento de auditoria salvo com sucesso para o cliente: ID={}", auditCustomerEntity.getId());
        } catch (Exception exception) {
            log.error("[PORT IN - AuditService.save] - Erro ao salvar evento de auditoria para o cliente: ID={}", auditCustomerEntity.getId(), exception);
            throw exception;
        }
    }

    @Override
    public Page<AuditCustomerEntity> findAll(Pageable pageable) {
        log.info("[PORT IN - AuditService.findAll] - Buscando todos as auditorias com paginação");
        return this.auditPortOut.findAll(pageable);
    }
}
