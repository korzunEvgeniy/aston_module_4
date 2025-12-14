package org.example.userservice.model;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
@CircuitBreaker
public class KafkaUserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public KafkaUserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(String operation, String email) {
        UserEvent event = new UserEvent(operation, email);
        kafkaTemplate.send(TOPIC, event);
    }
}
