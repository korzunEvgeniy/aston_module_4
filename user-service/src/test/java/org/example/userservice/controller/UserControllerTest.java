package org.example.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userservice.controller.model.UserDto;
import org.example.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserServiceImpl userService;

    @Test
    void getAllUsersTest() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alice@example.com");
        user.setAge(25);

        Mockito.when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Alex"));
    }

    @Test
    void createUserTest() throws Exception {
        UserDto input = new UserDto();
        input.setName("Bob");
        input.setEmail("bob@example.com");
        input.setAge(30);

        UserDto returned = new UserDto();
        returned.setId(2L);
        returned.setName("Bob");
        returned.setEmail("bob@example.com");
        returned.setAge(30);

        Mockito.when(userService.create(any(UserDto.class))).thenReturn(returned);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void getUserByIdFoundTest() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setAge(30);

        Mockito.when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void getUserByIdNotFoundTest() throws Exception {
        Mockito.when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserFoundTest() throws Exception {
        UserDto input = new UserDto();
        input.setId(1L);
        input.setName("Updated Name");
        input.setEmail("updated@example.com");
        input.setAge(35);

        Mockito.when(userService.update(any(UserDto.class))).thenReturn(input);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void updateUserNotFoundTest() throws Exception {
        UserDto input = new UserDto();
        input.setId(99L);
        input.setName("No User");
        input.setEmail("nouser@example.com");
        input.setAge(40);

        Mockito.when(userService.update(any(UserDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserTest() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
