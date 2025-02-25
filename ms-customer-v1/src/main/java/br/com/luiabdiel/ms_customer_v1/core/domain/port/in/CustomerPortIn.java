package br.com.luiabdiel.ms_customer_v1.core.domain.port.in;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerPortIn {

    CustomerEntity create(CustomerEntity customerEntity);

    CustomerEntity findById(Long id);

    Page<CustomerEntity> findAll(Pageable pageable);

    CustomerEntity update(Long id, CustomerEntity customerEntity);

    void deleteById(Long id);
}
