package com.example.juniorjavadeveloperbvpsoftware.repository;

import com.example.juniorjavadeveloperbvpsoftware.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    static Role role;

    @BeforeAll
    public static void setRole() {
        role = new Role();
        role.setName("USER");
    }

    @Test
    public void findByNameTest() {
        roleRepository.save(role);
        Role foundRole = roleRepository.findByName("USER");
        assertThat(foundRole).isEqualTo(role);
    }
}
