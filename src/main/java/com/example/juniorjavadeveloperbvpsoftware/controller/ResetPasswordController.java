package com.example.juniorjavadeveloperbvpsoftware.controller;

import com.example.juniorjavadeveloperbvpsoftware.dto.request.PasswordChangeRequest;
import com.example.juniorjavadeveloperbvpsoftware.dto.request.PasswordResetRequest;
import com.example.juniorjavadeveloperbvpsoftware.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService passwordService;

    @GetMapping("/api/auth/send/reset-password-email/{email}")
    public ResponseEntity<String> sendResetPasswordEmail(@PathVariable("email") String email) {
        passwordService.sendEmailResetPassword(email);
        return new ResponseEntity<>(String.format("The email to change your password has been sent successfully. Please check your email %s", email), HttpStatus.OK);
    }

    @PostMapping("/api/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordResetRequest request) {
        passwordService.changePasswordByToken(request);
        return new ResponseEntity<>("The password has been changed successfully.", HttpStatus.OK);
    }

    @PostMapping("/api/auth/change-password-auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordChangeRequest request,
                                                 Authentication authentication) {
        passwordService.changePassword(request.getNewPassword(), authentication.getName());

        return new ResponseEntity<>(String.format("The password has been changed successfully for user: %s", authentication.getName()), HttpStatus.OK);
    }
}
