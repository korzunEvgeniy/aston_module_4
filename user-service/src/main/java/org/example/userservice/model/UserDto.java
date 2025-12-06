package org.example.userservice.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotNull(message = "Имя не может быть пустым")
    @Size(min = 2, max = 20, message = "Имя должно быть от 2 до 50 символов")
    private String name;
    @NotNull(message = "Email обязателен")
    private String email;
    @Min(value = 18, message = "Возраст должен быть не менее 18")
    @Max(value = 100, message = "Возраст не может превышать 100")
    private Integer age;

    @Setter(AccessLevel.NONE)
    private final LocalDateTime createdAt = LocalDateTime.now();
}
