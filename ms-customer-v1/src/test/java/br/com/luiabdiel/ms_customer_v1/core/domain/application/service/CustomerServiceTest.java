package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.producer.CustomerProducer;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.CustomerIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.DataIntegratyViolationException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerProducer customerProducer;

    @Mock
    private CustomerIntegrator customerPortOut;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldCreateCustomerSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                expectedName,
                expectedEmail
        );
        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );
        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortOut.findByEmail(customerEntity.getEmail())).thenReturn(Optional.empty());
        when(this.modelMapper.map(customerRequestDto, CustomerEntity.class)).thenReturn(customerEntity);
        when(this.customerPortOut.save(any())).thenReturn(customerEntity);
        when(this.modelMapper.map(customerEntity, CustomerResponseDto.class)).thenReturn(customerResponseDto);

        CustomerResponseDto savedCustomer = this.customerService.create(customerRequestDto);

        assertNotNull(savedCustomer);
        assertEquals(expectedId, savedCustomer.getId());
        assertEquals(expectedName, savedCustomer.getName());
        assertEquals(expectedEmail, savedCustomer.getEmail());

        verify(this.customerPortOut, times(1)).findByEmail(customerEntity.getEmail());
        verify(this.customerPortOut, times(1)).save(any());
        verify(this.customerProducer, times(1)).sendCustomer(any());
    }

    @Test
    void shouldThrowDataIntegratyViolationWhenEmailAlreadyRegistered() {
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                expectedName,
                expectedEmail
        );

        when(this.customerPortOut.findByEmail(customerRequestDto.getEmail())).thenReturn(Optional.of(new CustomerEntity()));

        DataIntegratyViolationException thrownException = assertThrows(DataIntegratyViolationException.class, () -> {
            customerService.create(customerRequestDto);
        });

        assertEquals("Email already registered", thrownException.getMessage());
        verify(customerPortOut, times(1)).findByEmail(customerRequestDto.getEmail());
        verify(customerPortOut, never()).save(any());
        verify(customerProducer, never()).sendCustomer(any());
    }

    @Test
    void shouldFindCustomerByIdSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortOut.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
        CustomerEntity customer = this.customerService.findById(expectedId);

        verify(this.customerPortOut, times(1))
                .findById(expectedId);
        assertNotNull(customer);
        assertEquals(customerEntity.getId(), customer.getId());
    }

    @Test
    void shouldFindAllCustomersSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );
        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                expectedId,
                expectedName,
                expectedEmail
        );

        Page<CustomerEntity> expectedPage = new PageImpl<>(List.of(customerEntity));
        Pageable pageable = PageRequest.of(0, 10);

        when(this.customerPortOut.findAll(pageable)).thenReturn(expectedPage);
        doReturn(customerResponseDto).when(this.modelMapper).map(any(CustomerEntity.class), eq(CustomerResponseDto.class));

        Page<CustomerResponseDto> customers = this.customerService.findAll(pageable);

        verify(this.customerPortOut, times(1)).findAll(pageable);
        verify(this.modelMapper, atLeastOnce()).map(any(CustomerEntity.class), eq(CustomerResponseDto.class));

        assertNotNull(customers);
        assertEquals(1, customers.getTotalElements());
        assertEquals(expectedId, customers.getContent().get(0).getId());
        assertEquals(expectedName, customers.getContent().get(0).getName());
        assertEquals(expectedEmail, customers.getContent().get(0).getEmail());
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenCustomerNotFound() {
        var expectedId = 1L;

        when(this.customerPortOut.findById(expectedId)).thenReturn(Optional.empty());

        ObjectNotFoundException thrownException = assertThrows(ObjectNotFoundException.class, () -> {
            this.customerService.findById(expectedId);
        });

        assertEquals("Customer not found", thrownException.getMessage());
        verify(this.customerPortOut, times(1)).findById(expectedId);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        var expectedId = 1L;
        var expectedOldName = "Rob";
        var expectedNewName = "Robzin";
        var expectedEmail = "rob@email.com";

        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                expectedNewName,
                expectedEmail
        );
        CustomerEntity existingCustomer = new CustomerEntity(
                expectedId,
                expectedOldName,
                expectedEmail
        );
        CustomerEntity updatedCustomer = new CustomerEntity(
                expectedId,
                expectedNewName,
                expectedEmail
        );
        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                expectedId,
                expectedNewName,
                expectedEmail
        );

        when(this.customerPortOut.findById(expectedId)).thenReturn(Optional.of(existingCustomer));
        when(this.customerPortOut.findByEmail(expectedEmail)).thenReturn(Optional.of(existingCustomer));
        when(this.customerPortOut.save(any(CustomerEntity.class))).thenReturn(updatedCustomer);
        when(this.modelMapper.map(updatedCustomer, CustomerResponseDto.class)).thenReturn(customerResponseDto);

        CustomerResponseDto response = this.customerService.update(expectedId, customerRequestDto);

        verify(this.customerPortOut, times(1)).findById(expectedId);
        verify(this.customerPortOut, times(1)).findByEmail(expectedEmail);
        verify(this.customerPortOut, times(1)).save(any(CustomerEntity.class));
        verify(this.customerProducer, times(1)).sendCustomer(any());

        assertNotNull(response);
        assertEquals(expectedId, response.getId());
        assertEquals(expectedNewName, response.getName());
        assertEquals(expectedEmail, response.getEmail());
    }

    @Test
    void shouldThrowDataIntegratyViolationWhenEmailAlreadyRegisteredOnUpdate() {
        var expectedId = 1L;

        CustomerRequestDto customerRequestDto = new CustomerRequestDto("Robzin", "caramelo@email.com");
        CustomerEntity existingCustomer = new CustomerEntity(expectedId, "Rob", "rob@email.com");
        CustomerEntity anotherCustomer = new CustomerEntity(2L, "Caramelo", "caramelo@email.com");

        when(this.customerPortOut.findById(expectedId)).thenReturn(Optional.of(existingCustomer));
        when(this.customerPortOut.findByEmail(customerRequestDto.getEmail())).thenReturn(Optional.of(anotherCustomer));

        DataIntegratyViolationException thrownException = assertThrows(DataIntegratyViolationException.class, () -> {
            this.customerService.update(expectedId, customerRequestDto);
        });

        assertEquals("Email already registered", thrownException.getMessage());

        verify(this.customerPortOut, times(1)).findById(expectedId);
        verify(this.customerPortOut, times(1)).findByEmail(customerRequestDto.getEmail());
        verify(this.customerPortOut, never()).save(any());
        verify(this.customerProducer, never()).sendCustomer(any());
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity existingCustomer = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortOut.findById(expectedId)).thenReturn(Optional.of(existingCustomer));
        this.customerService.deleteById(expectedId);

        verify(this.customerPortOut, times(1)).findById(expectedId);
        verify(this.customerPortOut, times(1)).deleteById(expectedId);
        verify(this.customerProducer, times(1)).sendCustomer(any());
    }
}