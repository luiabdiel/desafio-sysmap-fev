package br.com.luiabdiel.ms_audit_v1.kafka.consumers;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.in.AuditPortIn;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.consumer.CustomerConsumer;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.AuditCustomerEvent;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerConsumerTest {

    @InjectMocks
    private CustomerConsumer customerConsumer;

    @Mock
    private AuditPortIn auditPortIn;

    @Test
    void shouldProcessCustomerEventAndSaveAudit() {
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        AuditCustomerEvent auditCustomerEvent = new AuditCustomerEvent(
                expectedName,
                expectedEmail,
                expectedEventType
        );

        doNothing().when(this.auditPortIn).save(any(AuditCustomerEntity.class));

        this.customerConsumer.listen(auditCustomerEvent);

        verify(this.auditPortIn, times(1))
                .save(argThat(arg ->
                        arg.getName().equals(expectedName) &&
                                arg.getEmail().equals(expectedEmail)
                ));
    }

    @Test
    void shouldHandleErrorWhenProcessCustomerEventFails() {
        var expectedCustomerId = 1L;
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        AuditCustomerEntity auditCustomerEntity = new AuditCustomerEntity(
                expectedCustomerId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        doThrow(new RuntimeException("Database error")).when(this.auditPortIn).save(any());
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.auditPortIn.save(auditCustomerEntity);
        });


        assertEquals("Database error", thrownException.getMessage());
        verify(this.auditPortIn, times(1)).save(any());
    }

}
