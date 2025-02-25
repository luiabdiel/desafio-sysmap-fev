package br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.repository;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByEmail(String email);
}
