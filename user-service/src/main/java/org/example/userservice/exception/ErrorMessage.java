package org.example.userservice.exception;

import java.time.LocalDateTime;

public record ErrorMessage(LocalDateTime timestamp, String message, String description) {
}
