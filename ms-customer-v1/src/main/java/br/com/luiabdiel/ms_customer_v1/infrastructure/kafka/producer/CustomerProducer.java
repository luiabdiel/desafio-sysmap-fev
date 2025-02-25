package br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.producer;

import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.event.CustomerEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.KafkaTopic.CUSTOMER_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendCustomer(CustomerEvent customerEvent) {
        log.info("[PRODUCER - CustomerProducer.sendCustomer] - Enviando evento para o Kafka: {}", customerEvent);
        try {
            String customerJson = this.objectMapper.writeValueAsString(customerEvent);

            this.kafkaTemplate.send(CUSTOMER_TOPIC, customerEvent.getId().toString(), customerJson);
            log.info("[PRODUCER - CustomerProducer.sendCustomer] - Evento enviado com sucesso para o Kafka. ID: {}", customerEvent.getId());
        } catch (Exception e) {
            log.error("[PRODUCER - CustomerProducer.sendCustomer] - Erro ao enviar evento para o Kafka. Evento: {}", customerEvent, e);
            e.printStackTrace();
        }
    }
}
