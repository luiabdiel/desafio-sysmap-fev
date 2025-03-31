package br.com.luiabdiel.ms_audit_v1.services;

import br.com.luiabdiel.ms_audit_v1.core.domain.application.service.AuditService;
import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.out.dto.AuditResponseDto;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.EventType;
import br.com.luiabdiel.ms_audit_v1.infrastructure.persistence.AuditIntegrator;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @InjectMocks
    private AuditService auditService;

    @Mock
    private AuditIntegrator auditPortOut;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldSaveAuditCustomerEntitySuccessfully() {
        var expectedCustomerId = 1L;
        var expectedName = "Rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        AuditCustomerEntity auditCustomerEntity = new AuditCustomerEntity(
                expectedCustomerId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        when(this.auditPortOut.save(any())).thenReturn(auditCustomerEntity);
        this.auditService.save(auditCustomerEntity);

        verify(this.auditPortOut, times(1))
                .save(argThat(arg ->
                                    Objects.equals(expectedCustomerId, arg.getCustomerId()) &&
                                    Objects.equals(expectedName, arg.getName()) &&
                                    Objects.equals(expectedEmail, arg.getEmail()) &&
                                    Objects.equals(expectedEventType, arg.getEventType())
                        )
                );
    }

    @Test
    void shouldHandleErrorWhenSaveFails() {
        var expectedCustomerId = 1L;
        var expectedName = "Rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        AuditCustomerEntity auditCustomerEntity = new AuditCustomerEntity(
                expectedCustomerId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        doThrow(new RuntimeException("Database error")).when(this.auditPortOut).save(any());

        assertThrows(RuntimeException.class, () -> this.auditService.save(auditCustomerEntity));
    }

    @Test
    void shouldFindAllAuditsSuccessfully() {
        var expectedId = 1L;
        var expectedCustomerId = 1L;
        var expectedName = "Rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        AuditCustomerEntity auditCustomerEntity = new AuditCustomerEntity(
                expectedId,
                expectedCustomerId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        AuditResponseDto auditResponseDto = new AuditResponseDto(
                expectedId,
                expectedCustomerId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        Page<AuditCustomerEntity> expectedPage = new PageImpl<>(List.of(auditCustomerEntity));
        Pageable pageable = PageRequest.of(0, 10);

        when(this.auditPortOut.findAll(pageable)).thenReturn(expectedPage);
        when(this.modelMapper.map(any(AuditCustomerEntity.class), eq(AuditResponseDto.class)))
                .thenReturn(auditResponseDto);

        Page<AuditResponseDto> customers = this.auditService.findAll(pageable);

        verify(this.auditPortOut, times(1)).findAll(pageable);
        verify(this.modelMapper, times(1)).map(any(AuditCustomerEntity.class), eq(AuditResponseDto.class));

        assertNotNull(customers);
        assertEquals(1, customers.getTotalElements());
        assertEquals(expectedName, customers.getContent().get(0).getName());
        assertEquals(expectedEmail, customers.getContent().get(0).getEmail());
    }
}