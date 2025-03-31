package br.com.luiabdiel.ms_audit_v1.api.rest.controller;

import br.com.luiabdiel.ms_audit_v1.core.domain.port.in.AuditPortIn;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.out.dto.AuditResponseDto;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditControllerImplTest {

    @InjectMocks
    private AuditControllerImpl auditController;

    @Mock
    private AuditPortIn auditPortIn;

    @Test
    void shouldReturnAllCustomersSuccessfully() {
        var expectedId = 1L;
        var expectedCustomerId = 1L;
        var expectedName = "Rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        AuditResponseDto auditResponseDto = new AuditResponseDto(
                expectedId,
                expectedCustomerId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        Page<AuditResponseDto> expectedPage = new PageImpl<>(List.of(auditResponseDto));
        Pageable pageable = PageRequest.of(0, 10);

        when(this.auditPortIn.findAll(pageable)).thenReturn(expectedPage);

        ResponseEntity<Page<AuditResponseDto>> response = this.auditController.findAll(pageable);

        verify(this.auditPortIn, times(1)).findAll(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(expectedName, response.getBody().getContent().get(0).getName());
        assertEquals(expectedEmail, response.getBody().getContent().get(0).getEmail());
    }
}