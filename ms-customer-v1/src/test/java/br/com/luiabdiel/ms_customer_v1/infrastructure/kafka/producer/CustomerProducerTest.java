package br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.producer;

import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.event.CustomerEvent;
import br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.event.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.KafkaTopic.CUSTOMER_TOPIC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerProducerTest {

    @InjectMocks
    private CustomerProducer customerProducer;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldSendCustomerEventSuccessfully() throws Exception {
        Long expectedId = 1L;
        var expectedName = "Rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        CustomerEvent customerEvent = new CustomerEvent(
          expectedId,
          expectedName,
          expectedEmail,
          expectedEventType
        );
        String customerJson = "{\"id\":1,\"name\":\"Rob\",\"email\":\"rob@email.com\",\"eventType\":\"CREATE\"}";

        when(this.objectMapper.writeValueAsString(any())).thenReturn(customerJson);
        this.customerProducer.sendCustomer(customerEvent);

        verify(this.objectMapper, times(1)).writeValueAsString(customerEvent);
        verify(this.kafkaTemplate, times(1)).send(CUSTOMER_TOPIC, expectedId.toString(), customerJson);
    }

    @Test
    void shouldHandleExceptionWhenSerializationFails() throws Exception {
        Long expectedId = 1L;
        var expectedName = "Rob";
        var expectedEmail = "rob@email.com";
        var expectedEventType = EventType.CREATE;

        CustomerEvent customerEvent = new CustomerEvent(
                expectedId,
                expectedName,
                expectedEmail,
                expectedEventType
        );

        when(this.objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Serialization error"));

        this.customerProducer.sendCustomer(customerEvent);

        verify(this.objectMapper, times(1)).writeValueAsString(customerEvent);
        verify(this.kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }
}