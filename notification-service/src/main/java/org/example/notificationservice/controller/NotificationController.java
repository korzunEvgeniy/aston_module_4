package org.example.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.notificationservice.model.NotificationModelAssembler;
import org.example.notificationservice.service.EmailService;
import org.example.notificationservice.service.UserEvent;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notification API", description = "API for notification's management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;
    private final NotificationModelAssembler assembler;

    @Operation(summary = "Отправка сообщения на почту пользователю")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Сообщение на почту отправлено"))
    @PostMapping("/send-email")
    public ResponseEntity<EntityModel<UserEvent>> sendEmail(@RequestParam String email,
                                                            @RequestParam String operation) {

        UserEvent event = new UserEvent(operation, email);

        String message = switch (operation) {
            case "CREATE" -> "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
            case "DELETE" -> "Здравствуйте! Ваш аккаунт был удалён.";
            default -> "Неизвестная операция.";
        };
        emailService.sendEmail(email, "Уведомление об аккаунте", message);
        return ResponseEntity.ok(assembler.toModel(event));
    }
}
