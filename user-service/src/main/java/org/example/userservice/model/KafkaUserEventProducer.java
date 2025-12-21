package org.example.userservice.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaUserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public KafkaUserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @CircuitBreaker
    public void sendUserEvent(String operation, String email) {
        UserEvent event = new UserEvent(operation, email);
        kafkaTemplate.send(TOPIC, event);
    }

    public void sendFallback(String operation, String email, Throwable throwable) {
        log.warn("CircuitBreaker OPEN: Kafka unavailable. Op: {}, Email: {}, Error: {}",
                operation, email, throwable.getMessage());
        // Можно сохранить в DLQ или БД для retry
    }
}
