package com.example.juniorjavadeveloperbvpsoftware.service.impl;

import com.example.juniorjavadeveloperbvpsoftware.dto.request.PasswordResetRequest;
import com.example.juniorjavadeveloperbvpsoftware.exception.NullEntityReferenceException;
import com.example.juniorjavadeveloperbvpsoftware.model.ResetPassword;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.repository.ResetPasswordRepository;
import com.example.juniorjavadeveloperbvpsoftware.security.config.DBConfig;
import com.example.juniorjavadeveloperbvpsoftware.service.ResetPasswordService;
import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ResetPasswordImpl implements ResetPasswordService {
    private final ResetPasswordRepository resetPasswordRepository;
    private final EmailServiceImpl emailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DBConfig dbConfig;

    public ResetPasswordImpl(ResetPasswordRepository repository, EmailServiceImpl userService, UserService userService1, PasswordEncoder passwordEncoder, DBConfig dbConfig) {
        this.resetPasswordRepository = repository;
        this.emailService = userService;
        this.userService = userService1;
        this.passwordEncoder = passwordEncoder;
        this.dbConfig = dbConfig;
    }

    @Override
    public ResetPassword create(ResetPassword resetPassword) {
        if (resetPassword == null) {
            log.error("Attempted to save null resetPassword");
            throw new NullEntityReferenceException("resetPassword cannot be 'null'");
        } else if (resetPassword.getEmail().isEmpty() || resetPassword.getVerificationToken().isEmpty()) {
            log.error("Attempted to save empty email or token");
            throw new NullPointerException("Attempted to save empty email or token");
        } else {
            return resetPasswordRepository.save(resetPassword);
        }
    }

    @Override
    public void delete(String email) {
        resetPasswordRepository.delete(resetPasswordRepository.findByEmail(email));
    }

    @Override
    public void sendEmailResetPassword(String email) {
        String token = UUID.randomUUID().toString();

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setEmail(email);
        resetPassword.setVerificationToken(token);
        create(resetPassword);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset Password!");
        mailMessage.setText(String.format("""
                To reset your password you need:\s
                {\s
                 'verificationToken' : '%s',\s
                 'newPassword' : 'enter a new password'\s
                }""", token));
        mailMessage.setFrom(dbConfig.getEmail());

        emailService.sendEmail(mailMessage);
    }

    @Override
    public void changePassword(String newPassword, String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            log.error("Attempt to change password for non-existing user: {}", email);
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        if (newPassword.isEmpty()) {
            log.error("Attempted to change password with empty string for user: {}", email);
            throw new NullEntityReferenceException("Password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
        log.info("Password changed successfully for user: {}", email);
    }

    @Override
    public void changePasswordByToken(PasswordResetRequest resetRequest) {
        ResetPassword resetPassword = resetPasswordRepository.findByVerificationToken(resetRequest.getVerificationToken());
        if (resetPassword == null) {
            log.error("Attempt to change password with invalid token: {}", resetRequest.getVerificationToken());
            throw new EntityNotFoundException("Invalid token");
        }
        String email = resetPassword.getEmail();
        if (resetPasswordRepository.findByEmail(email) == null || email.isEmpty()) {
            log.error("Attempt to change password for non-existing user: {}", email);
            throw new EntityNotFoundException("User not found with email: " + email);
        }

        changePassword(resetRequest.getNewPassword(), email);
        delete(email);
    }
}
