package br.com.luiabdiel.ms_email_v1.infrastructure.kafka.consumer;

import br.com.luiabdiel.ms_email_v1.core.domain.port.in.EmailPortIn;
import br.com.luiabdiel.ms_email_v1.core.domain.port.in.dto.EmailDto;
import br.com.luiabdiel.ms_email_v1.infrastructure.kafka.KafkaTopicName;
import br.com.luiabdiel.ms_email_v1.infrastructure.kafka.event.EmailCustomerEvent;
import br.com.luiabdiel.ms_email_v1.infrastructure.kafka.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCustomerConsumer {

    private final EmailPortIn emailPortIn;

    @KafkaListener(id = "emailCustomerConsumer", topics = KafkaTopicName.CUSTOMER_TOPIC, containerFactory = "emailCustomerEventKafkaListenerContainerFactory")
    public void listen(EmailCustomerEvent emailCustomerEvent) {
        log.info("[CONSUMER - EmailCustomerConsumer.listen] - Evento recebido do tópico [{}]: {}", KafkaTopicName.CUSTOMER_TOPIC, emailCustomerEvent);

        if(emailCustomerEvent.getEventType() == EventType.CREATE) {
            EmailDto emailDto = new EmailDto();

            emailDto.setTo(emailCustomerEvent.getEmail());
            emailDto.setSubject("🎉 Bem-vindo à nossa comunidade! 🎉");
            String body = "<html>"
                    + "<body>"
                    + "<h2>Olá " + emailCustomerEvent.getName() + ",</h2>"
                    + "<p>Estamos super empolgados em tê-lo conosco!</p>"
                    + "<p><strong>Seja bem-vindo ao nosso serviço.</strong></p>"
                    + "<p>Para começar, temos muitos recursos incríveis esperando por você!</p>"
                    + "<p>Se tiver alguma dúvida ou precisar de assistência, nossa equipe de suporte está sempre disponível para ajudar.</p>"
                    + "<br><br>"
                    + "<p><i>Atenciosamente,</i><br>Equipe de Suporte</p>"
                    + "<hr>"
                    + "<footer>"
                    + "<p>Se você não se cadastrou em nossa plataforma, por favor, ignore este e-mail.</p>"
                    + "</footer>"
                    + "</body>"
                    + "</html>";
            emailDto.setBody(body);

            this.emailPortIn.sendEmail(emailDto);
            log.info("[CONSUMER - EmailCustomerConsumer.listen] - Preparando para enviar e-mail de boas-vindas para o cliente: {}", emailCustomerEvent.getEmail());
        }
    }
}
