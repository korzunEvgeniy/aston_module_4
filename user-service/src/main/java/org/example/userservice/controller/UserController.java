package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.exception.UserNotFoundException;
import org.example.userservice.model.KafkaUserEventProducer;
import org.example.userservice.model.UserDto;
import org.example.userservice.model.UserModelAssembler;
import org.example.userservice.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User API", description = "API for user's management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KafkaUserEventProducer kafkaUserEventProducer;
    private final UserModelAssembler assembler;

    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Список пользователей"))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.findAll().stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(users,
                WebMvcLinkBuilder.linkTo(UserController.class).withSelfRel()));
    }

    @Operation(summary = "Получить пользователя по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь с таким айди найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким айди не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable Long id) {
        UserDto dto = userService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(dto));
    }


    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Пользователь успешно создан"))
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(@RequestBody UserDto dto) {
        UserDto created = userService.create(dto);
        kafkaUserEventProducer.sendUserEvent("CREATE", dto.getEmail());
        return ResponseEntity.ok(assembler.toModel(created));
    }

    @Operation(summary = "Обновление существующего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping()
    public ResponseEntity<EntityModel<UserDto>> updateUser(@Valid @RequestBody UserDto dto) {
        UserDto updated = userService.update(dto);
        if (updated == null) {
            throw new UserNotFoundException("User not found");
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Пользователь успешно удален"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        userService.delete(id);
        kafkaUserEventProducer.sendUserEvent("DELETE", user.getEmail());
        return ResponseEntity.noContent().build();
    }
}
