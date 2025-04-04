package br.com.luiabdiel.ms_customer_v1.core.domain.port.in;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerPortIn {

    CustomerResponseDto create(CustomerRequestDto customerRequestDto);

    CustomerResponseDto findById(Long id);

    Page<CustomerResponseDto> findAll(Pageable pageable);

    CustomerResponseDto update(Long id, CustomerRequestDto customerRequestDto);

    void deleteById(Long id);
}
