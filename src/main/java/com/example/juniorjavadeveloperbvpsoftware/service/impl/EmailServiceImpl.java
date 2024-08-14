package com.example.juniorjavadeveloperbvpsoftware.service.impl;

import com.example.juniorjavadeveloperbvpsoftware.exception.NullEmailException;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.repository.UserRepository;
import com.example.juniorjavadeveloperbvpsoftware.security.config.DBConfig;
import com.example.juniorjavadeveloperbvpsoftware.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("emailService")
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final DBConfig dbConfig;

    public EmailServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender, DBConfig dbConfig) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.dbConfig = dbConfig;
    }

    @Async
    public void sendEmail(SimpleMailMessage emailMessage) {
        try {
            javaMailSender.send(emailMessage);
            log.info("Email sent to user: {}", emailMessage.getTo());
        } catch (MailException e) {
            log.info("Email wasn't sent to user: {}", emailMessage.getTo());
        }
    }

    @Override
    public void sendEmailConfirmation(String email, String url) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/api/auth/email-confirm/" + url);
        mailMessage.setFrom(dbConfig.getEmail());
        sendEmail(mailMessage);
    }

    @Override
    public void resendEmailConfirmation(String email) {
        String url = userRepository.findByEmail(email).getUrlForVerifiedEmail();
        if (url == null) {
            throw new NullEmailException("Make sure you have entered the correct email address or your email address may have been verified, so try logging in");
        }
        sendEmailConfirmation(email, url);
    }

    @Override
    public String confirmEmail(String url) {
        User user = userRepository.findByUrlForVerifiedEmail(url);
        if (user == null) {
            log.error("Invalid email verification attempt. No user found associated with url: {}", url);
            throw new UsernameNotFoundException("Invalid email verification attempt. No user found associated");
        }
        user.setEmailVerified(true);
        user.setUrlForVerifiedEmail(null);
        userRepository.save(user);
        log.info("Email verified successfully for user: {}", user.getEmail());
        return String.format("Email verified successfully for user: %s", user.getEmail());
    }
}
