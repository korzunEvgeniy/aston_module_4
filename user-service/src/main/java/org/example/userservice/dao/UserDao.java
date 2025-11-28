package org.example.userservice.dao;

import org.example.userservice.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
