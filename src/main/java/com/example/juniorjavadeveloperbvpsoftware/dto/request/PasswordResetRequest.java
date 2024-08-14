package com.example.juniorjavadeveloperbvpsoftware.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PasswordResetRequest {
    @NotEmpty
    private String verificationToken;
    @NotEmpty
    private String newPassword;
}
