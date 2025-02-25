package br.com.luiabdiel.ms_audit_v1.infrastructure.persistence;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.EventType;
import br.com.luiabdiel.ms_audit_v1.infrastructure.persistence.repository.AuditRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditIntegratorTest {

    @InjectMocks
    private AuditIntegrator auditIntegrator;

    @Mock
    private AuditRepository auditRepository;

    @Test
    void shouldSaveAuditCustomerInIntegratorEntitySuccessfully() {
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

        when(this.auditRepository.save(any())).thenReturn(auditCustomerEntity);
        this.auditIntegrator.save(auditCustomerEntity);

        verify(this.auditRepository, times(1))
                .save(argThat(arg ->
                        Objects.equals(expectedCustomerId, arg.getCustomerId()) &&
                                Objects.equals(expectedName, arg.getName()) &&
                                Objects.equals(expectedEmail, arg.getEmail()) &&
                                Objects.equals(expectedEventType, arg.getEventType())
                ));
    }

    @Test
    void shouldHandleExceptionWhenSaveFails() {
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

        doThrow(new RuntimeException("Database error")).when(this.auditRepository).save(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.auditIntegrator.save(auditCustomerEntity);
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.auditRepository, times(1)).save(auditCustomerEntity);
    }

    @Test
    void shouldFindAllAuditsSuccessfully() {
        var expectedId = 2L;
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

        Page<AuditCustomerEntity> expectedPage = new PageImpl<>(List.of(auditCustomerEntity));
        Pageable pageable = PageRequest.of(0, 10);

        when(this.auditRepository.findAll(pageable)).thenReturn(expectedPage);
        Page<AuditCustomerEntity> audits = this.auditIntegrator.findAll(pageable);

        verify(this.auditRepository, times(1)).findAll(pageable);
        assertNotNull(audits);
        assertEquals(expectedId, audits.getContent().get(0).getId());
    }

    @Test
    void shouldHandleExceptionWhenFindAllFails() {
        Pageable pageable = PageRequest.of(0, 10);
        doThrow(new RuntimeException("Database error")).when(this.auditRepository).findAll(pageable);

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.auditIntegrator.findAll(pageable);
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.auditRepository, times(1)).findAll(pageable);
    }
}