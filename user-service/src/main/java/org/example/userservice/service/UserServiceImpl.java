package org.example.userservice.service;

import org.example.userservice.controller.model.UserDto;
import org.example.userservice.dao.UserDao;
import org.example.userservice.dao.entity.User;
import org.example.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto create(UserDto userDto) {
        User saved = UserMapper.toEntity(userDto);
        return UserMapper.toDto(userDao.save(saved));
    }

    @Override
    public UserDto findById(Long id) {
        return userDao.findById(id).stream().map(UserMapper::toDto).findAny().orElseThrow();
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public UserDto update(UserDto userDto) {
        User existing = userDao.findById(userDto.getId()).orElseThrow();
        existing = (UserMapper.toEntity(userDto));
        return UserMapper.toDto(userDao.save(existing));
    }

    @Override
    public void delete(Long id) {
        userDao.deleteById(id);
    }
}
