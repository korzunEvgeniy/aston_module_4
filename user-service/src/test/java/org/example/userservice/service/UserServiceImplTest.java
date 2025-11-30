package org.example.userservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.userservice.model.UserDto;
import org.example.userservice.dao.UserDao;
import org.example.userservice.dao.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        UserDto inputDto = new UserDto();
        inputDto.setId(null);
        inputDto.setName("John");
        inputDto.setEmail("john@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John");
        savedUser.setEmail("john@example.com");

        when(userDao.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.create(inputDto);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getName(), result.getName());
        assertEquals(savedUser.getEmail(), result.getEmail());

        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testFindByIdFound() {
        Long id = 1L;
        User foundUser = new User();
        foundUser.setId(id);
        when(userDao.findById(id)).thenReturn(Optional.of(foundUser));

        UserDto result = userService.findById(id);

        assertNotNull(result);
        assertEquals(result.getId(), foundUser.getId());
        verify(userDao).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findById(id));
        verify(userDao).findById(id);
    }

    @Test
    void testFindAll() {
        List<User> users = List.of(new User(), new User());
        when(userDao.findAll()).thenReturn(users);

        List<UserDto> result = userService.findAll();

        assertEquals(users.size(), result.size());
        verify(userDao).findAll();
    }

    @Test
    void testUpdate() {
        UserDto inputDto = new UserDto();
        inputDto.setId(1L);
        inputDto.setName("John Updated");
        inputDto.setEmail("john.updated@example.com");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John");
        existingUser.setEmail("john@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");

        when(userDao.findById(inputDto.getId())).thenReturn(Optional.of(existingUser));
        when(userDao.save(any(User.class))).thenReturn(updatedUser);

        UserDto result = userService.update(inputDto);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());

        verify(userDao, times(1)).findById(inputDto.getId());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testDelete() {
        Long id = 1L;

        doNothing().when(userDao).deleteById(id);

        userService.delete(id);

        verify(userDao).deleteById(id);
    }
}
