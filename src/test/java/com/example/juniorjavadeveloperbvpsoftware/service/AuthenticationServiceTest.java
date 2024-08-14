package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.dto.response.LoginResponse;
import com.example.juniorjavadeveloperbvpsoftware.exception.NotVerifiedEmailException;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenGenerator;
import com.example.juniorjavadeveloperbvpsoftware.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    AuthenticationServiceImpl authenticationService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtTokenGenerator jwtTokenGenerator;
    @Mock
    Authentication authentication;

    @Test
    @DisplayName("Should success login")
    void login_test() {
        User user = new User();
        user.setEmail("email");
        user.setEmailVerified(true);

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(jwtTokenGenerator.generateToken(user.getEmail())).thenReturn("jwt");
        LoginResponse response = authenticationService.login(user, "1");

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should return NotVerifiedEmailException when user not verified email")
    void login_should_return_NotVerifiedEmailException_test() {
        User user = new User();
        user.setEmail("email");
        user.setEmailVerified(false);

        assertThrows(NotVerifiedEmailException.class, () -> authenticationService.login(user, "1"));
    }
}
