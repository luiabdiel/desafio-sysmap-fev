package br.com.luiabdiel.ms_customer_v1.infrastructure.persistence;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.repository.CustomerRepository;
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
class CustomerIntegratorTest {

    @InjectMocks
    private CustomerIntegrator customerIntegrator;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void shouldFindCustomerByEmailSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerRepository.findByEmail(expectedEmail)).thenReturn(Optional.of(customerEntity));
        Optional<CustomerEntity> customerO = this.customerIntegrator.findByEmail(expectedEmail);

        verify(this.customerRepository, times(1)).findByEmail(expectedEmail);
        assert(customerO.isPresent());
        assert(customerO.get().getEmail().equals(expectedEmail));
    }

    @Test
    void shouldHandleExceptionWhenFindByEmailFails() {
        doThrow(new RuntimeException("Database error")).when(customerRepository).findByEmail(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            customerIntegrator.findByEmail(any());
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(customerRepository, times(1)).findByEmail(any());
    }

    @Test
    void shouldSaveCustomerSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerRepository.save(any())).thenReturn(customerEntity);
        CustomerEntity customer = this.customerIntegrator.save(customerEntity);

        verify(this.customerRepository, times(1))
                .save(argThat(arg ->
                                Objects.equals(expectedEmail, arg.getEmail()) &&
                                Objects.equals(expectedName, arg.getName()) &&
                                Objects.equals(expectedId, arg.getId())
                )
        );
    }

    @Test
    void shouldHandleExceptionWhenSaveFails() {
        doThrow(new RuntimeException("Database error")).when(this.customerRepository).save(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.customerIntegrator.save(any());
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.customerRepository, times(1)).save(any());
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

        when(this.customerRepository.findAll(pageable)).thenReturn(expectedPage);
        Page<CustomerEntity> customers = this.customerIntegrator.findAll(pageable);

        verify(this.customerRepository, times(1)).findAll(pageable);
        assertNotNull(customers);
        assertEquals(expectedId, customers.getContent().get(0).getId());
    }

    @Test
    void shouldHandleExceptionWhenFindAllFails() {
        Pageable pageable = PageRequest.of(0, 10);
        doThrow(new RuntimeException("Database error")).when(this.customerRepository).findAll(pageable);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.customerIntegrator.findAll(pageable);
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.customerRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldDeleteCustomerByIdSuccessfully() {
        var expectedId = 1L;

        doNothing().when(this.customerRepository).deleteById(expectedId);
        this.customerIntegrator.deleteById(expectedId);

        verify(this.customerRepository, times(1)).deleteById(expectedId);
    }

    @Test
    void shouldHandleExceptionWhenDeleteByIdFails() {
        doThrow(new RuntimeException("Database error")).when(this.customerRepository).deleteById(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.customerIntegrator.deleteById(any());
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.customerRepository, times(1)).deleteById(any());
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

        when(this.customerRepository.findById(expectedId)).thenReturn(Optional.of(customerEntity));
        Optional<CustomerEntity> customerO = this.customerIntegrator.findById(expectedId);

        verify(this.customerRepository, times(1)).findById(expectedId);
        assertNotNull(customerO);
        assert(customerO.get().getId().equals(expectedId));
    }

    @Test
    void shouldHandleExceptionWhenFindByIdFails() {
        doThrow(new RuntimeException("Database error")).when(this.customerRepository).findById(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.customerIntegrator.findById(any());
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.customerRepository, times(1)).findById(any());
    }
}