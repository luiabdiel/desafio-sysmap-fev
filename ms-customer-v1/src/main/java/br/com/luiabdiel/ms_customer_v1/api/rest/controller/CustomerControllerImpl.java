package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.CustomerPortIn;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerControllerImpl implements CustomerController {

    private final CustomerPortIn customerPortIn;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerRequestDto customerRequestDto) {
        log.info("[CONTROLLER - CustomerControllerImpl.create] - Criando cliente com email: {}", customerRequestDto.getEmail());
        CustomerEntity customerEntity = this.modelMapper.map(customerRequestDto, CustomerEntity.class);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(this.customerPortIn.create(customerEntity).getId())
                .toUri();

        log.info("[CONTROLLER - CustomerControllerImpl.create] - Cliente criado com sucesso.");
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable(value = "id") Long id) {
        log.info("[CONTROLLER - CustomerControllerImpl.findById] - Buscando cliente por ID: {}", id);
        CustomerEntity customerEntity = this.customerPortIn.findById(id);
        CustomerResponseDto customerDto = this.modelMapper.map(customerEntity, CustomerResponseDto.class);

        log.info("[CONTROLLER - CustomerControllerImpl.findById] - Cliente encontrado com ID: {}", id);
        return ResponseEntity.ok().body(customerDto);
    }

    @Override
    public ResponseEntity<Page<CustomerResponseDto>> findAll(@ParameterObject Pageable pageable) {
        log.info("[CONTROLLER - CustomerControllerImpl.findAll] - Buscando todos os clientes com paginação");
        Page<CustomerEntity> customerEntities = this.customerPortIn.findAll(pageable);

        Page<CustomerResponseDto> customerDto = customerEntities.map(
                entity -> this.modelMapper.map(entity, CustomerResponseDto.class)
        );

        log.info("[CONTROLLER - CustomerControllerImpl.findAll] - Total de clientes encontrados: {}", customerDto.getTotalElements());
        return ResponseEntity.ok().body(customerDto);
    }

    @Override
    public ResponseEntity<CustomerResponseDto> update (
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid CustomerRequestDto customerRequestDto)
    {
        log.info("[CONTROLLER - CustomerControllerImpl.update] - Atualizando cliente ID: {}", id);
        CustomerEntity customer = this.modelMapper.map(customerRequestDto, CustomerEntity.class);
        CustomerEntity customerEntity = this.customerPortIn.update(id, customer);
        CustomerResponseDto customerResponse = this.modelMapper.map(customerEntity, CustomerResponseDto.class);

        log.info("[CONTROLLER - CustomerControllerImpl.update] - Cliente atualizado com sucesso. ID: {}", id);
        return ResponseEntity.ok().body(customerResponse);
    }

    @Override
    public ResponseEntity<CustomerResponseDto> deleteById(@PathVariable(value = "id") Long id) {
        log.info("[CONTROLLER - CustomerControllerImpl.deleteById] - Excluindo cliente ID: {}", id);
        this.customerPortIn.deleteById(id);

        log.info("[CONTROLLER - CustomerControllerImpl.deleteById] - Cliente excluído com sucesso. ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
