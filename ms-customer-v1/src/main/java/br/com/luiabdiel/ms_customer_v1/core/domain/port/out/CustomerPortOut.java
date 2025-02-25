package br.com.luiabdiel.ms_customer_v1.core.domain.port.out;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerPortOut {

    Optional<CustomerEntity> findByEmail(String email);

    CustomerEntity save(CustomerEntity customerEntity);

    Page<CustomerEntity> findAll(Pageable pageable);

    void deleteById(Long id);

    Optional<CustomerEntity> findById(Long id);
}
