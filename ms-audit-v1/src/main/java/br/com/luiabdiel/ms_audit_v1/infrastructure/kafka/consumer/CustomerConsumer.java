package br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.consumer;

import br.com.luiabdiel.ms_audit_v1.core.domain.entity.AuditCustomerEntity;
import br.com.luiabdiel.ms_audit_v1.core.domain.port.in.AuditPortIn;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.KafkaTopicName;
import br.com.luiabdiel.ms_audit_v1.infrastructure.kafka.event.AuditCustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerConsumer {

    private final AuditPortIn auditPortIn;

    @KafkaListener(id = "auditCustomerConsumer", topics = KafkaTopicName.CUSTOMER_TOPIC, containerFactory = "auditCustomerEventKafkaListenerContainerFactory")
    public void listen(AuditCustomerEvent customerEvent) {
        log.info("[CONSUMER - CustomerConsumer.listen] - Mensagem recebida do tópico [{}]: {}", KafkaTopicName.CUSTOMER_TOPIC, customerEvent);

        AuditCustomerEntity auditCustomerEntity = new AuditCustomerEntity(
                customerEvent.getId(),
                customerEvent.getName(),
                customerEvent.getEmail(),
                customerEvent.getEventType()
        );

        log.info("[CONSUMER - CustomerConsumer.listen] - Preparando para salvar evento de auditoria para o cliente ID: {} | Nome: {} | E-mail: {}",
                customerEvent.getId(), customerEvent.getName(), customerEvent.getEmail());
        this.auditPortIn.save(auditCustomerEntity);
        log.info("[CONSUMER - CustomerConsumer.listen] - Evento de auditoria processado e salvo com sucesso para o cliente ID: {} | Nome: {} | E-mail: {}",
                customerEvent.getId(), customerEvent.getName(), customerEvent.getEmail());
    }
}
