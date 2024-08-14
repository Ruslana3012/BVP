package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.exception.NullEntityReferenceException;
import com.example.juniorjavadeveloperbvpsoftware.exception.UserAlreadyExistsException;
import com.example.juniorjavadeveloperbvpsoftware.model.Role;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.repository.RoleRepository;
import com.example.juniorjavadeveloperbvpsoftware.repository.UserRepository;
import com.example.juniorjavadeveloperbvpsoftware.service.impl.UserServiceImpl;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;
    @Mock
    private EmailService emailService;
    User user;
    Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setName("USER");

        user = new User();
        user.setId(1L);
        user.setEmail("email@gmail.com");
        user.setPassword("1");
        user.setUrlForVerifiedEmail("hdh21ewc2e");
        user.setEmailVerified(true);
    }

    @Test
    @DisplayName("Should save new User correctly")
    void save_user_it_should_return_user_test() {
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        Mockito.when(roleRepository.findByName("USER")).thenReturn(role);

        User actual = userService.saveUser(user);

        verify(emailService).sendEmailConfirmation(user.getEmail(), user.getUrlForVerifiedEmail());

        assertEquals(user.getEmail(), actual.getEmail());
        assertNotNull(actual, "The saved user should not be null");
        assertNotNull(actual.getPassword(), "The password of the saved user should be encoded");
        assertNotNull(actual.getUrlForVerifiedEmail(), "The saved user should have a verification URL");
    }

    @Test
    @DisplayName("Should save return NullEntityReferenceException when trying to save a null User")
    void save_user_it_should_return_NullEntityReferenceException_test() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> userService.saveUser(null));
        assertTrue(exception.getMessage().contains("User cannot be 'null'"));
    }

    @Test
    @DisplayName("Should save return NullPointerException when trying to save empty userName or password")
    void save_user_it_should_return_NullPointerException_test() {
        User emptyUser = new User();
        emptyUser.setEmail("");
        emptyUser.setPassword("");

        Exception exception = assertThrows(NullPointerException.class, () -> userService.saveUser(emptyUser));
        assertTrue(exception.getMessage().contains("Attempted to save empty email or password"));
    }

    @Test
    @DisplayName("Should save return UserAlreadyExistsException when user already exists with this email")
    void save_user_it_should_return_UserAlreadyExistsException_test() {
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user));
        assertTrue(exception.getMessage().contains("Attempted to save user with existing email"));
    }

    @Test
    @DisplayName("Should return user by id")
    void read_by_id_should_return_user_test() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        User actual = userService.readById(user.getId());

        assertEquals(user.getEmail(), actual.getEmail());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when user is not found")
    void read_by_id_should_return_EntityNotFoundException_test() {
        Mockito.when(userRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.readById(4L));
        verify(userRepository, times(1)).findById(4L);
    }

    @Test
    @DisplayName("Should return update users")
    void update_user_test() {
        User updateUser = new User();
        updateUser.setEmail("email@gmail.com");
        updateUser.setPassword("updatepassword");
        Mockito.when(userRepository.save(updateUser)).thenReturn(updateUser);
        User actual = userRepository.save(updateUser);

        assertEquals(updateUser, actual);
        verify(userRepository).save(updateUser);
    }

    @Test
    @DisplayName("Should return NullEntityReferenceException when try to update 'null' user")
    public void update_user_NullEntityReferenceException_test() {
        assertThrows(NullEntityReferenceException.class, () -> userService.updateUser(null));
    }

    @Test
    @DisplayName("Should return NullPointerException when try to update  empty email or password")
    public void update_user_NullPointerException_test() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> userService.updateUser(user));
    }

    @Test
    @DisplayName("Should return user by email")
    void find_by_email_should_return_user_test() {
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User actual = userService.findByEmail(user.getEmail());

        assertEquals(user.getEmail(), actual.getEmail());
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("Should return null by email")
    void find_by_email_should_return_null_test() {
        given(userRepository.findByEmail("email"))
                .willReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.findByEmail("email"));
    }

    @Test
    @DisplayName("Should delete user")
    void delete_test() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        userService.delete(user.getId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Should return ThrowException when user not found")
    public void delete_should_return_ThrowException_test() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.delete(4L));
    }

    @Test
    @DisplayName("Should return all users")
    void get_all_users_test() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        List<User> users = Arrays.asList(user, user2);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> actual = userService.getAllUsers();

        assertThat(actual).isNotEmpty();
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("Should return user by userName")
    void load_user_by_userName_test() {
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User actual = (User) userService.loadUserByUsername(user.getEmail());
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("Should return UsernameNotFoundException")
    void load_user_by_userName_should_return_UsernameNotFoundException_test() {
        Mockito.when(userRepository.findByEmail("email")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("email"));
    }
}
