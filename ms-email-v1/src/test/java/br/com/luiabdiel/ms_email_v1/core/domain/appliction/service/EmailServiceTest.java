package br.com.luiabdiel.ms_email_v1.core.domain.appliction.service;

import br.com.luiabdiel.ms_email_v1.core.domain.port.in.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @Test
    void shouldSendEmailSuccessfully() throws MessagingException {
        var expectedEmail = "rob@email.com";
        var expectedSubject = "Test Subject";
        var expectedBody = "Test Body";
        EmailDto emailDto = new EmailDto(
                expectedEmail,
                expectedSubject,
                expectedBody
        );

        when(this.javaMailSender.createMimeMessage()).thenReturn(this.mimeMessage);
        this.emailService.sendEmail(emailDto);

        verify(this.javaMailSender, times(1)).send(this.mimeMessage);
    }

    @Test
    void shouldHandleErrorWhenSendFails() throws MessagingException {
        var expectedEmail = "rob@email.com";
        var expectedSubject = "Test Subject";
        var expectedBody = "Test Body";
        EmailDto emailDto = new EmailDto(
                expectedEmail,
                expectedSubject,
                expectedBody
        );

        when(this.javaMailSender.createMimeMessage()).thenReturn(this.mimeMessage);
        doThrow(new MailException("Email error") {}).when(this.javaMailSender).send(any(MimeMessage.class));

        this.emailService.sendEmail(emailDto);
    }
}