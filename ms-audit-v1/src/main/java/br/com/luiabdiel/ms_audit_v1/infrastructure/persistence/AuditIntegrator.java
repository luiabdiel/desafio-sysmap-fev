package br.com.luiabdiel.ms_audit_v1.infrastructure.persistence;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.out.AuditPortOut;
import br.com.luiabdiel.ms_audit_v1.infrastructure.persistence.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditIntegrator implements AuditPortOut {

    private final AuditRepository auditRepository;

    @Override
    public AuditCustomerEntity save(AuditCustomerEntity auditCustomerEntity) {
        log.info("[PORT OUT - AuditIntegrator.save] - Tentando salvar evento de auditoria para o cliente: ID={}, Name={}, Email={}",
                auditCustomerEntity.getId(), auditCustomerEntity.getName(), auditCustomerEntity.getEmail());

        try {
            log.info("[PORT OUT - AuditIntegrator.save] - Evento de auditoria salvo com sucesso para o cliente: ID={}", auditCustomerEntity.getId());
            return this.auditRepository.save(auditCustomerEntity);
        } catch (Exception exception) {
            log.error("[PORT OUT - AuditIntegrator.save] - Erro ao salvar evento de auditoria para o cliente: ID={}", auditCustomerEntity.getId(), exception);
            throw exception;
        }
    }

    @Override
    public Page<AuditCustomerEntity> findAll(Pageable pageable) {
        log.info("[PORT OUT - AuditIntegrator.findAll] - Iniciando a busca de eventos de auditoria com paginação: Página={}, Tamanho={}",
                pageable.getPageNumber(), pageable.getPageSize());
        try {
            return this.auditRepository.findAll(pageable);
        } catch (Exception exception) {
            log.error("[PORT OUT - AuditIntegrator.findAll] - Erro ao buscar eventos de auditoria", exception);
            throw exception;
        }
    }
}
