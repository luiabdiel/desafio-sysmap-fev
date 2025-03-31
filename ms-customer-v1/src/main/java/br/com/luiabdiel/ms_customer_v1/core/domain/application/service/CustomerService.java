package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.CustomerPortIn;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.event.CustomerEvent;
import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.event.EventType;
import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.producer.CustomerProducer;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.CustomerIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.DataIntegratyViolationException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements CustomerPortIn {

    private final CustomerProducer customerProducer;
    private final CustomerIntegrator customerPortOut;
    private final ModelMapper modelMapper;

    @Override
    public CustomerResponseDto create(CustomerRequestDto customerRequestDto) {
        log.info("[PORT IN - CustomerService.create] - Criando cliente: {}", customerRequestDto.getEmail());
         this.customerPortOut.
                findByEmail(customerRequestDto.getEmail())
                .ifPresent(x -> { throw new DataIntegratyViolationException("Email already registered"); });

        CustomerEntity savedCustomer = this.customerPortOut.save(
                this.modelMapper.map(customerRequestDto, CustomerEntity.class)
        );

        this.customerProducer.sendCustomer(new CustomerEvent(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                EventType.CREATE
        ));

        log.info("[PORT IN - CustomerService.create] - Cliente criado com sucesso: {}", savedCustomer.getId());
        return this.modelMapper.map(savedCustomer, CustomerResponseDto.class);
    }

    @Override
    public CustomerEntity findById(Long id) {
        log.info("[PORT IN - CustomerService.findById] - Buscando cliente por ID: {}", id);
        return this.
                customerPortOut.
                findById(id).
                orElseThrow(() -> {
                    log.error("[PORT IN - CustomerService.findById] - Cliente não encontrado: {}", id);
                    return new ObjectNotFoundException("Customer not found");
                });
    }

    @Override
    public Page<CustomerResponseDto> findAll(Pageable pageable) {
        log.info("[PORT IN - CustomerService.findAll] - Buscando todos os clientes com paginação");
        Page<CustomerEntity> customerEntities = this.customerPortOut.findAll(pageable);

        return customerEntities.map(entity -> this.modelMapper.map(entity, CustomerResponseDto.class));
    }

    @Override
    public CustomerEntity update(Long id, CustomerEntity customerEntity) {
        log.info("[PORT IN - CustomerService.update] - Atualizando cliente ID: {}", id);
        CustomerEntity customer = this.findById(id);

        this.customerPortOut.
                findByEmail(customerEntity.getEmail())
                .ifPresent(existingCustomer -> {
                    if(!existingCustomer.getId().equals(id)) {
                        throw new DataIntegratyViolationException("Email already registered");
                    }
                });

        customer.setName(customerEntity.getName());
        customer.setEmail(customerEntity.getEmail());

        CustomerEntity savedCustomer = this.customerPortOut.save(customer);

        this.customerProducer.sendCustomer(new CustomerEvent(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                EventType.UPDATE
        ));

        log.info("[PORT IN - CustomerService.update] - Cliente atualizado com sucesso: {}", id);
        return savedCustomer;
    }

    @Override
    public void deleteById(Long id) {
        log.info("[PORT IN - CustomerService.deleteById] - Excluindo cliente ID: {}", id);
        CustomerEntity customerFromDb = this.findById(id);

        this.customerPortOut.deleteById(id);
        this.customerProducer.sendCustomer(new CustomerEvent(
                customerFromDb.getId(),
                customerFromDb.getName(),
                customerFromDb.getEmail(),
                EventType.DELETE
        ));
        log.info("[PORT IN - CustomerService.deleteById] - Cliente excluído com sucesso: {}", id);
    }
}
