package org.example.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<Void> sendEmail(@RequestParam String email,
                                          @RequestParam String operation) {
        String message = switch (operation) {
            case "CREATE" -> "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
            case "DELETE" -> "Здравствуйте! Ваш аккаунт был удалён.";
            default -> "Неизвестная операция.";
        };
        emailService.sendEmail(email, "Уведомление об аккаунте", message);
        return ResponseEntity.ok().build();
    }
}
