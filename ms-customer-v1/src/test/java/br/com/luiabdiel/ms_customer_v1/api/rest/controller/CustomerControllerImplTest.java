package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.CustomerEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.CustomerPortIn;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.CustomerRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.CustomerResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerImplTest {

    @InjectMocks
    private CustomerControllerImpl customerController;

    @Mock
    private CustomerPortIn customerPortIn;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldCreateCustomerSuccessfully() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                expectedName,
                expectedEmail
        );
        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortIn.create(any())).thenReturn(customerResponseDto);

        ResponseEntity<CustomerResponseDto> response = this.customerController.create(customerRequestDto);

        verify(this.customerPortIn, times(1)).create(any());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldReturnCustomerWhenFindingByIdSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerEntity customerEntity = new CustomerEntity(
                expectedId,
                expectedName,
                expectedEmail
        );

        when(this.customerPortIn.findById(expectedId)).thenReturn(customerEntity);
        ResponseEntity<CustomerResponseDto> response = this.customerController.findById(expectedId);

        verify(this.customerPortIn, times(1)).findById(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
    }

    @Test
    void shouldReturnAllCustomersSuccessfully() {
        var expectedId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";

        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                expectedId,
                expectedName,
                expectedEmail
        );

        Page<CustomerResponseDto> expectedPage = new PageImpl<>(List.of(customerResponseDto));;
        Pageable pageable = PageRequest.of(0, 10);

        when(this.customerPortIn.findAll(pageable)).thenReturn(expectedPage);

        ResponseEntity<Page<CustomerResponseDto>> response = this.customerController.findAll(pageable);

        verify(this.customerPortIn, times(1)).findAll(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(expectedName, response.getBody().getContent().get(0).getName());
        assertEquals(expectedEmail, response.getBody().getContent().get(0).getEmail());
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
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

        when(this.modelMapper.map(customerRequestDto, CustomerEntity.class)).thenReturn(customerEntity);
        when(this.modelMapper.map(customerEntity, CustomerResponseDto.class)).thenReturn(customerResponseDto);
        when(this.customerPortIn.update(eq(expectedId), any(CustomerEntity.class))).thenReturn(customerEntity);

        ResponseEntity<CustomerResponseDto> response = this.customerController.update(expectedId, customerRequestDto);

        verify(this.customerPortIn, times(1)).update(eq(expectedId), any(CustomerEntity.class));
        verify(this.modelMapper, times(1)).map(customerRequestDto, CustomerEntity.class);
        verify(this.modelMapper, times(1)).map(customerEntity, CustomerResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedId, response.getBody().getId());
        assertEquals(expectedName, response.getBody().getName());
        assertEquals(expectedEmail, response.getBody().getEmail());
    }


    @Test
    void shouldDeleteCustomerSuccessfully() {
        var expectedId = 1L;

        doNothing().when(this.customerPortIn).deleteById(expectedId);
        ResponseEntity<CustomerResponseDto> response = this.customerController.deleteById(expectedId);

        verify(this.customerPortIn, times(1)).deleteById(expectedId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}