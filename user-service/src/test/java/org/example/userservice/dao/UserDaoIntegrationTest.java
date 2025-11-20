package org.example.userservice.dao;

import org.example.userservice.dao.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("integrationtest")
public class UserDaoIntegrationTest {

    @Autowired
    private UserDao userDao;

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        User saved = userDao.save(user);
        Optional<User> retrieved = userDao.findById(saved.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Test User");
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("one@example.com");

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("two@example.com");

        userDao.save(user1);
        userDao.save(user2);

        assertThat(userDao.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }
}
