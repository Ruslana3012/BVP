package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.dto.request.PasswordResetRequest;
import com.example.juniorjavadeveloperbvpsoftware.model.ResetPassword;

public interface ResetPasswordService {
    ResetPassword create(ResetPassword resetPassword);
    void delete(String email);
    void sendEmailResetPassword(String email);
    void changePassword(String password, String email);
    void changePasswordByToken(PasswordResetRequest resetRequest);
}
