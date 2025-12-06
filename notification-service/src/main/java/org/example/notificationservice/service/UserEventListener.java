package org.example.notificationservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private final EmailService emailService;

    public UserEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(UserEvent event) {
        String subject = "Уведомление об аккаунте";
        String message;
        switch (event.operation()) {
            case "CREATE" -> message = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
            case "DELETE" -> message = "Здравствуйте! Ваш аккаунт был удалён.";
            default -> {
                return;
            }
        }
        emailService.sendEmail(event.email(), subject, message);
    }
}
