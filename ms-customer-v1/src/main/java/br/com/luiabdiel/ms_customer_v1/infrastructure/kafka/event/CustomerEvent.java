package br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerEvent {

    private Long id;

    private String name;

    private String email;

    private EventType eventType;
}
