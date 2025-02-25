package br.com.luiabdiel.ms_email_v1.infrastructure.kafka.consumer;

import br.com.luiabdiel.ms_email_v1.core.domain.port.in.EmailPortIn;
import br.com.luiabdiel.ms_email_v1.core.domain.port.in.dto.EmailDto;
import br.com.luiabdiel.ms_email_v1.infrastructure.kafka.event.EmailCustomerEvent;
import br.com.luiabdiel.ms_email_v1.infrastructure.kafka.event.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailCustomerConsumerTest {

    @InjectMocks
    private EmailCustomerConsumer emailCustomerConsumer;

    @Mock
    private EmailPortIn emailPortIn;

    @Captor
    private ArgumentCaptor<EmailDto> emailDtoCaptor;

    @Test
    void shouldSendWelcomeEmailWhenCustomerIsCreated() {
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        EmailCustomerEvent emailCustomerEvent = new EmailCustomerEvent(
                expectedName,
                expectedEmail,
                expectedEventType
        );

        doNothing().when(this.emailPortIn).sendEmail(any(EmailDto.class));
        this.emailCustomerConsumer.listen(emailCustomerEvent);

        verify(this.emailPortIn, times(1)).sendEmail(this.emailDtoCaptor.capture());
        EmailDto capturedEmail = this.emailDtoCaptor.getValue();

        assertEquals("rob@email.com", capturedEmail.getTo());
        assertEquals("🎉 Bem-vindo à nossa comunidade! 🎉", capturedEmail.getSubject());
        assertTrue(capturedEmail.getBody().contains("Olá rob,"));
    }

    @Test
    void shouldNotSendEmailWhenEventTypeIsNotCreate() {
        var expectedName = "rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.UPDATE;

        EmailCustomerEvent emailCustomerEvent = new EmailCustomerEvent(
                expectedName,
                expectedEmail,
                expectedEventType
        );

        this.emailCustomerConsumer.listen(emailCustomerEvent);

        verify(this.emailPortIn, never()).sendEmail(any(EmailDto.class));
    }
}
