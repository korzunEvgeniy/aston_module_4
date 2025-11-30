package org.example.notificationservice.controller;

import org.example.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    @Test
    void sendEmail_CREATE_CallsServiceWithCorrectMessage() throws Exception {
        // Выполнение запроса
        mockMvc.perform(post("/api/notifications/send-email")
                        .param("email", "user@example.com")
                        .param("operation", "CREATE"))
                .andExpect(status().isOk());

        verify(emailService).sendEmail(
                eq("user@example.com"),
                eq("Уведомление об аккаунте"),
                eq("Здравствуйте! Ваш аккаунт на сайте был успешно создан.")
        );

        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendEmail_DELETE_CallsServiceWithCorrectMessage() throws Exception {
        mockMvc.perform(post("/api/notifications/send-email")
                        .param("email", "user@example.com")
                        .param("operation", "DELETE"))
                .andExpect(status().isOk());

        verify(emailService).sendEmail(
                eq("user@example.com"),
                eq("Уведомление об аккаунте"),
                eq("Здравствуйте! Ваш аккаунт был удалён.")
        );

        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendEmail_UnknownOperation_CallsServiceWithDefaultMessage() throws Exception {
        mockMvc.perform(post("/api/notifications/send-email")
                        .param("email", "user@example.com")
                        .param("operation", "UNKNOWN"))
                .andExpect(status().isOk());

        verify(emailService).sendEmail(
                eq("user@example.com"),
                eq("Уведомление об аккаунте"),
                eq("Неизвестная операция.")
        );

        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}
