package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CustomerController {

    @PostMapping(value = "/customers")
    ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerRequestDto customerRequestDto);

    @GetMapping(value = "/customers/{id}")
    ResponseEntity<CustomerResponseDto> findById(@PathVariable(value = "id") Long id);

    @GetMapping(value = "/customers")
    ResponseEntity<Page<CustomerResponseDto>> findAll(Pageable pageable);

    @PutMapping(value = "/customers/{id}")
    ResponseEntity<CustomerResponseDto> update (
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid CustomerRequestDto customerRequestDto);

    @DeleteMapping(value = "/customers/{id}")
    ResponseEntity<CustomerResponseDto> deleteById(@PathVariable(value = "id") Long id);
}
