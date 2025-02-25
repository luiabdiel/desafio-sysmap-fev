package br.com.luiabdiel.ms_customer_v1.shared.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.HashMap;
import java.util.Map;

import static br.com.luiabdiel.ms_customer_v1.infrastructure.kafka.KafkaTopic.CUSTOMER_TOPIC;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${kafka.topic.partitions:1}")
    private int numPartitions;

    @Value("${kafka.topic.replication-factor:1}")
    private short replicationFactor;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(CUSTOMER_TOPIC, numPartitions, replicationFactor);
    }
}
