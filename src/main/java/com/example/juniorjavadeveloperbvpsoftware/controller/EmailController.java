package com.example.juniorjavadeveloperbvpsoftware.controller;

import com.example.juniorjavadeveloperbvpsoftware.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/api/auth/resend/email-confirmation/{email}")
    public ResponseEntity<String> resendEmailConfirmation(@PathVariable("email") String email) {
        emailService.resendEmailConfirmation(email);
        return new ResponseEntity<>(String.format("The email confirmation has been successfully resent. Please check your email %s and confirm your registration.", email),
                HttpStatus.OK);
    }

    @GetMapping("/api/auth/email-confirm/{token}")
    public ResponseEntity<String> emailConfirmation(@PathVariable("token") String token) {
        return new ResponseEntity<>(emailService.confirmEmail(token), HttpStatus.OK);
    }
}
