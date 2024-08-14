package com.example.juniorjavadeveloperbvpsoftware.repository;

import com.example.juniorjavadeveloperbvpsoftware.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    User user;

    @BeforeEach
    public void setUser() {
        user = new User();
        user.setEmail("email@gmail.com");
        user.setPassword("1");
        user.setUrlForVerifiedEmail("hdh21ewc2e");
        user.setEmailVerified(true);
    }

    @Test
    public void findByEmailTest() {
        User expected = userRepository.save(user);
        User actual = userRepository.findByEmail("email@gmail.com");

        assertEquals(expected, actual);
    }

    @Test
    public void findByUrlForVerifiedEmailTest() {
        User expected = userRepository.save(user);
        User actual = userRepository.findByUrlForVerifiedEmail("hdh21ewc2e");

        assertEquals(expected, actual);
    }
}
