package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.exception.NullEntityReferenceException;
import com.example.juniorjavadeveloperbvpsoftware.model.Role;
import com.example.juniorjavadeveloperbvpsoftware.repository.RoleRepository;
import com.example.juniorjavadeveloperbvpsoftware.service.impl.RoleServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @InjectMocks
    RoleServiceImpl roleService;
    @Mock
    private RoleRepository roleRepository;
    Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setName("USER");
        role.setId(1);
    }

    @Test
    @DisplayName("Should save new Role correctly")
    void create_should_return_Role_test() {
        Mockito.when(roleRepository.save(role)).thenReturn(role);
        Role actual = roleRepository.save(role);

        assertEquals(role, actual);
        assertNotNull(actual, "The saved role should not be null");
    }

    @Test
    @DisplayName("Should return NullEntityReferenceException when role is null")
    void create_should_return_NullEntityReferenceException_test() {
        assertThrows(NullEntityReferenceException.class, () -> roleService.create(null));
    }

    @Test
    @DisplayName("Should return role by id")
    void read_by_id_should_return_role_by_id_test() {
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.ofNullable(role));

        Role actual = roleService.readById(1L);
        assertEquals(role, actual);
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when role is not found")
    void read_by_id_should_return_EntityNotFoundException_test() {
        assertThrows(EntityNotFoundException.class, () -> roleService.readById(4L));
    }

    @Test
    @DisplayName("Should return new update role")
    void update_should_return_new_update_role() {
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.ofNullable(role));
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        roleService.update(role);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    @DisplayName("Should return NullEntityReferenceException when role is null")
    void update_should_return_NullEntityReferenceException_test() {
        assertThrows(NullEntityReferenceException.class, () -> roleService.update(null));
    }


    @Test
    @DisplayName("Should return EntityNotFoundException when role is not found")
    void update_should_return_EntityNotFoundException_test() {
        Role roleThatNotExist = new Role();
        roleThatNotExist.setId(4);
        roleThatNotExist.setName("name");

        Mockito.when(roleRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.update(roleThatNotExist));
    }

    @Test
    @DisplayName("Should delete role")
    void delete_test() {
        Mockito.when(roleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(role));
        roleService.delete(role.getId());

        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    @DisplayName("Should return ThrowException when role not found")
    public void delete_should_return_ThrowException_test() {
        Mockito.when(roleRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.delete(4L));
    }

    @Test
    @DisplayName("Should return all roles")
    void get_all_roles_test() {
        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("role");

        List<Role> roles = Arrays.asList(role, role2);
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        List<Role> actual = roleService.getAllRoles();

        assertThat(actual).isNotEmpty();
        assertEquals(2, actual.size());
    }
}
