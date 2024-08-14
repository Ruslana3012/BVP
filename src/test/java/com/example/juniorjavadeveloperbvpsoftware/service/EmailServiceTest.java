package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.repository.UserRepository;
import com.example.juniorjavadeveloperbvpsoftware.security.config.DBConfig;
import com.example.juniorjavadeveloperbvpsoftware.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @InjectMocks
    EmailServiceImpl emailService;
    @Mock
    UserRepository userRepository;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    DBConfig dbConfig;

    SimpleMailMessage mailMessage;

    @BeforeEach
    void setUp() {
        mailMessage = new SimpleMailMessage();
        mailMessage.setTo("email");
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8091/api/auth/email-confirm/" + "url");
        mailMessage.setFrom("brv123456789@outlook.com");
    }

    @Test
    @DisplayName("Should send email")
    void send_email_test() {
        emailService.sendEmail(mailMessage);
        verify(javaMailSender, times(1)).send(mailMessage);
    }

    @Test
    @DisplayName("Should send email confirmation to user")
    void send_email_Confirmation_test() {
        Mockito.when(dbConfig.getEmail()).thenReturn("email");
        emailService.sendEmailConfirmation("email", "url");
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should success confirm email")
    void confirm_email_test() {
        User user = new User();
        user.setEmailVerified(false);
        Mockito.when(userRepository.findByUrlForVerifiedEmail("url")).thenReturn(user);

        emailService.confirmEmail("url");

        assertTrue(user.isEmailVerified());
        verify(userRepository, times(1)).findByUrlForVerifiedEmail("url");
        verify(userRepository, times(1)).save(user);

    }

    @Test
    @DisplayName("Should fail confirm email return UsernameNotFoundException")
    void confirm_email_UsernameNotFoundException_test() {
        assertThrows(UsernameNotFoundException.class, () -> emailService.confirmEmail(null));
        verify(userRepository, never()).save(any(User.class));
    }
}
