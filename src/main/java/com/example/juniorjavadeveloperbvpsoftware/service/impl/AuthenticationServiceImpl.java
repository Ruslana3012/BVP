package com.example.juniorjavadeveloperbvpsoftware.service.impl;

import com.example.juniorjavadeveloperbvpsoftware.dto.response.LoginResponse;
import com.example.juniorjavadeveloperbvpsoftware.exception.NotVerifiedEmailException;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenGenerator;
import com.example.juniorjavadeveloperbvpsoftware.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenGenerator jwtTokenGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    public LoginResponse login(User user, String password) {
        if (!user.isEmailVerified()) {
            log.warn("Attempted login with unverified email: {}", user.getEmail());
            throw new NotVerifiedEmailException("Attempted login with unverified email");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        password
                )
        );
        log.info("User authenticated successfully: {}", user.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenGenerator.generateToken(user.getEmail());
        String jwt = jwtTokenGenerator.generateRefreshToken(user.getEmail());

        LoginResponse response = new LoginResponse(user.getEmail(),token, jwt);
        log.info("JWT refresh token generated for user: {}", user.getEmail());

        return response;
    }
}
