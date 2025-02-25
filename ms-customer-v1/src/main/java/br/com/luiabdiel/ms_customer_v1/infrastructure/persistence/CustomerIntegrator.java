package br.com.luiabdiel.ms_customer_v1.infrastructure.persistence;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.CustomerPortOut;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerIntegrator implements CustomerPortOut {

    private final CustomerRepository customerRepository;

    @Override
    public Optional<CustomerEntity> findByEmail(String email) {
        log.info("[PORT OUT - CustomerIntegrator.findByEmail] - Buscando por e-mail: {}", email);
        try {
            return this.customerRepository.findByEmail(email);
        } catch (Exception exception) {
            log.error("[PORT OUT - CustomerIntegrator.findByEmail] - Erro ao buscar por e-mail: {}", email);
            throw exception;
        }
    }

    @Override
    public CustomerEntity save(CustomerEntity customerEntity) {
        log.info("[PORT OUT - CustomerIntegrator.save] - Salvando cliente: {}", customerEntity);
        try {
            return this.customerRepository.save(customerEntity);
        } catch (Exception exception) {
            log.error("[PORT OUT - CustomerIntegrator.save] - Erro ao salvar cliente: {}", customerEntity, exception);
            throw exception;
        }
    }

    @Override
    public Page<CustomerEntity> findAll(Pageable pageable) {
        log.info("[PORT OUT - CustomerIntegrator.findAll] - Buscando todos os clientes");
        try {
            return this.customerRepository.findAll(pageable);
        } catch (Exception exception) {
            log.warn("[PORT OUT - CustomerIntegrator.findAll] - Erro ao buscar todos os clientes", exception);
            throw exception;
        }
    }

    @Override
    public void deleteById(Long id) {
        log.info("[PORT OUT - CustomerIntegrator.deleteById] - Deletando cliente com ID: {}", id);
        try {
            this.customerRepository.deleteById(id);
        } catch (Exception exception) {
            log.warn("[PORT OUT - CustomerIntegrator.deleteById] - Erro ao deletar cliente com ID: {}", id, exception);
            throw exception;
        }
    }

    @Override
    public Optional<CustomerEntity> findById(Long id) {
        log.info("[PORT OUT - CustomerIntegrator.findById] - Buscando cliente com ID: {}", id);
        try {
            return this.customerRepository.findById(id);
        } catch (Exception exception) {
            log.warn("[PORT OUT - CustomerIntegrator.findById] - Erro ao buscar cliente com ID: {}", id, exception);
            throw exception;
        }
    }
}
