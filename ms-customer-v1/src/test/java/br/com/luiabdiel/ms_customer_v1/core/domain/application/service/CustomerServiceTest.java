package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.producer.CustomerProducer;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.CustomerIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.DataIntegratyViolationException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
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

    @Test
    void shouldCreateCustomerSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortOut.findByEmail(customerEntity.getEmail())).thenReturn(Optional.empty());
        when(this.customerPortOut.save(customerEntity)).thenReturn(customerEntity);

        CustomerEntity createdCustomer = this.customerService.create(customerEntity);

        assertNotNull(createdCustomer);

        verify(this.customerPortOut, times(1)).findByEmail(customerEntity.getEmail());
        verify(this.customerPortOut, times(1)).save(
                argThat(arg ->
                        Objects.equals(expectedId, createdCustomer.getId()) &&
                        Objects.equals(expectedName, createdCustomer.getName()) &&
                        Objects.equals(expectedEmail, createdCustomer.getEmail())
                )
        );
        verify(this.customerProducer, times(1)).sendCustomer(any());
    }

    @Test
    void shouldThrowDataIntegratyViolationWhenEmailAlreadyRegistered() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortOut.findByEmail(customerEntity.getEmail())).thenReturn(Optional.of(new CustomerEntity()));

        DataIntegratyViolationException thrownException = assertThrows(DataIntegratyViolationException.class, () -> {
            customerService.create(customerEntity);
        });

        assertEquals("Email already registered", thrownException.getMessage());
        verify(customerPortOut, times(1)).findByEmail(customerEntity.getEmail());
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

        Page<CustomerEntity> expectedPage = new PageImpl<>(List.of(customerEntity));
        Pageable pageable = PageRequest.of(0, 10);

        when(this.customerPortOut.findAll(pageable)).thenReturn(expectedPage);
        Page<CustomerEntity> customers = this.customerService.findAll(pageable);

        verify(this.customerPortOut, times(1)).findAll(pageable);
        assertNotNull(customers);
        assertEquals(expectedId, customers.getContent().get(0).getId());
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

        CustomerEntity existingCustomer = new CustomerEntity(expectedId, "Rob", "rob@email.com");
        CustomerEntity updatedCustomer = new CustomerEntity(expectedId, "Robzin", "rob@email.com");

        when(this.customerPortOut.findById(existingCustomer.getId())).thenReturn(Optional.of(existingCustomer));
        when(this.customerPortOut.findByEmail(updatedCustomer.getEmail())).thenReturn(Optional.of(existingCustomer));
        when(this.customerPortOut.save(existingCustomer)).thenReturn(updatedCustomer);

        CustomerEntity customer = this.customerService.update(updatedCustomer.getId(), updatedCustomer);

        verify(this.customerPortOut, times(1)).findById(existingCustomer.getId());
        verify(this.customerPortOut, times(1)).findByEmail(updatedCustomer.getEmail());
        verify(this.customerPortOut, times(1)).save(existingCustomer);
        verify(this.customerProducer, times(1)).sendCustomer(any());
        assertNotNull(customer);
        assertEquals(updatedCustomer.getName(), customer.getName());
    }

    @Test
    void shouldThrowDataIntegratyViolationWhenEmailAlreadyRegisteredOnUpdate() {
        var expectedId = 1L;

        CustomerEntity existingCustomer = new CustomerEntity(expectedId, "Rob", "rob@email.com");
        CustomerEntity updatedCustomer = new CustomerEntity(expectedId, "Robzin", "caramelo@email.com");
        CustomerEntity anotherCustomer = new CustomerEntity(2L, "Caramelo", "caramelo@email.com");

        when(this.customerPortOut.findById(expectedId)).thenReturn(Optional.of(existingCustomer));
        when(this.customerPortOut.findByEmail(updatedCustomer.getEmail())).thenReturn(Optional.of(anotherCustomer));

        DataIntegratyViolationException thrownException = assertThrows(DataIntegratyViolationException.class, () -> {
            this.customerService.update(expectedId, updatedCustomer);
        });

        assertEquals("Email already registered", thrownException.getMessage());
        verify(this.customerPortOut, times(1)).findById(expectedId);
        verify(this.customerPortOut, times(1)).findByEmail(updatedCustomer.getEmail());
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