package org.example.userservice.service;

import org.example.userservice.controller.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);
    UserDto findById(Long id);
    List<UserDto> findAll();
    UserDto update(UserDto userDto);
    void delete(Long id);

}
