package com.example.juniorjavadeveloperbvpsoftware.service;

public interface EmailService {
    void sendEmailConfirmation(String email, String url);

    void resendEmailConfirmation(String email);

    String confirmEmail(String url);
}
