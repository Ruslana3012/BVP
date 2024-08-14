package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.dto.request.PasswordResetRequest;
import com.example.juniorjavadeveloperbvpsoftware.exception.NullEntityReferenceException;
import com.example.juniorjavadeveloperbvpsoftware.model.ResetPassword;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.repository.ResetPasswordRepository;
import com.example.juniorjavadeveloperbvpsoftware.security.config.DBConfig;
import com.example.juniorjavadeveloperbvpsoftware.service.impl.EmailServiceImpl;
import com.example.juniorjavadeveloperbvpsoftware.service.impl.ResetPasswordImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ResetPasswordTest {
    @InjectMocks
    ResetPasswordImpl resetPasswordService;
    @Mock
    ResetPasswordRepository resetPasswordRepository;
    @Mock
    EmailServiceImpl emailService;
    @Mock
    UserService userService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    DBConfig dbConfig;
    ResetPassword resetPassword;

    @BeforeEach
    void setUp() {
        resetPassword = new ResetPassword();
        resetPassword.setEmail("plaintext@example.com");
        resetPassword.setVerificationToken("token");
    }

    @Test
    @DisplayName("Should create ResetPassword")
    void create_test() {
        Mockito.when(resetPasswordRepository.save(Mockito.any(ResetPassword.class))).thenReturn(resetPassword);
        ResetPassword actual = resetPasswordService.create(resetPassword);
        assertEquals(resetPassword, actual);
    }

    @Test
    @DisplayName("Should return NullEntityReferenceException when try to create 'null' ResetPassword")
    public void create_NullEntityReferenceException_test() {
        assertThrows(NullEntityReferenceException.class, () -> resetPasswordService.create(null));
    }

    @Test
    @DisplayName("Should return NullPointerException when try to create  empty ResetPassword")
    public void create_NullPointerException_test() {
        ResetPassword emptyReset = new ResetPassword();
        assertThrows(NullPointerException.class, () -> resetPasswordService.create(emptyReset));
    }

    @Test
    @DisplayName("Should delete ResetPassword")
    void delete_test() {
        Mockito.when(resetPasswordRepository.findByEmail("email")).thenReturn(resetPassword);
        resetPasswordService.delete("email");
        verify(resetPasswordRepository, times(1)).delete(resetPassword);
    }

    @Test
    @DisplayName("Should send email reset password")
    public void send_email_reset_password_test() {
        Mockito.when(resetPasswordRepository.save(any(ResetPassword.class))).thenReturn(resetPassword);
        Mockito.when(dbConfig.getEmail()).thenReturn("email");
        resetPasswordService.sendEmailResetPassword("email");

        verify(emailService, times(1)).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should change password")
    public void change_password_test() {
        User user = new User();
        user.setPassword("1");
        user.setEmail("email");
        Mockito.when(userService.findByEmail("email")).thenReturn(user);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        resetPasswordService.changePassword("newPassword", "email");

        verify(userService, times(1)).findByEmail("email");
        verify(passwordEncoder, times(1)).encode("newPassword");
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when try to change password but user is not found")
    void change_password_should_return_EntityNotFoundException_test() {
        Mockito.when(userService.findByEmail("email")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> resetPasswordService.changePassword("newPassword", "email"));
        verify(userService, times(1)).findByEmail("email");
    }

    @Test
    @DisplayName("Should return NullEntityReferenceException when try change 'null' password")
    public void change_password_should_return_NullEntityReferenceException_test() {
        User userMock = Mockito.mock(User.class);
        Mockito.when(userService.findByEmail(anyString())).thenReturn(userMock);

        assertThrows(NullEntityReferenceException.class, () -> resetPasswordService.changePassword("", "email"));
    }

    @Test
    @DisplayName("Should change password by token")
    void change_password_by_token_test() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNewPassword("newPassword");
        request.setVerificationToken("theRightToken");

        User user = new User();
        user.setEmail(resetPassword.getEmail());
        user.setPassword("oldPassword");

        Mockito.when(this.resetPasswordRepository.findByVerificationToken(anyString())).thenReturn(resetPassword);
        Mockito.when(this.resetPasswordRepository.findByEmail(anyString())).thenReturn(resetPassword);
        Mockito.when(this.userService.findByEmail(anyString())).thenReturn(user);
        Mockito.when(this.passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        assertDoesNotThrow(() -> resetPasswordService.changePasswordByToken(request));
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when ResetPassword is 'null'")
    public void change_password_by_token_should_return_EntityNotFoundException_test() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNewPassword("newPassword");
        request.setVerificationToken("theWrongToken");

        Mockito.when(resetPasswordRepository.findByVerificationToken(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> resetPasswordService.changePasswordByToken(request));
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when User is 'null' or empty")
    void change_password_by_token_should_return_EntityNotFoundException_no_existing_user_test() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNewPassword("newPassword");
        request.setVerificationToken("theWrongToken");

        Mockito.when(resetPasswordRepository.findByVerificationToken(anyString())).thenReturn(resetPassword);
        Mockito.when(resetPasswordRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> resetPasswordService.changePasswordByToken(request));
    }
}
