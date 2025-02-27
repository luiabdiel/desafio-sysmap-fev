package br.com.luiabdiel.ms_email_v1.core.domain.appliction.service;

import br.com.luiabdiel.ms_email_v1.core.domain.port.in.EmailPortIn;
import br.com.luiabdiel.ms_email_v1.core.domain.port.in.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements EmailPortIn {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailDto emailDto) {
        log.info("[PORT IN - EmailService.sendEmail] - Iniciando o envio de e-mail para {}", emailDto.getTo());

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(emailDto.getTo());
            helper.setFrom("noreply@email.com");
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getBody(), true);

            this.javaMailSender.send(mimeMessage);
            log.info("[PORT IN - EmailService.sendEmail] - E-mail enviado com sucesso para {}", emailDto.getTo());
        }  catch (MessagingException | MailException e) {
            log.error("[PORT IN - EmailService.sendEmail] - Erro ao enviar e-mail para {}: {}", emailDto.getTo(), e.getMessage(), e);
        }
    }
}
