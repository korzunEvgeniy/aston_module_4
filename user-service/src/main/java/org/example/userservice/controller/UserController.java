package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.exception.UserNotFoundException;
import org.example.userservice.model.KafkaUserEventProducer;
import org.example.userservice.model.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KafkaUserEventProducer kafkaUserEventProducer;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto dto = userService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        UserDto created = userService.create(dto);
        kafkaUserEventProducer.sendUserEvent("CREATE", dto.getEmail());
        return ResponseEntity.ok(created);
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto dto) {
        UserDto updated = userService.update(dto);
        if (updated == null) {
            throw new UserNotFoundException("User not found");
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        userService.delete(id);
        kafkaUserEventProducer.sendUserEvent("DELETE", user.getEmail());
        return ResponseEntity.noContent().build();
    }
}
