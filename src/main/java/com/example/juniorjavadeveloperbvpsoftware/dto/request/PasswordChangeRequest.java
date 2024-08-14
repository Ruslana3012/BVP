package com.example.juniorjavadeveloperbvpsoftware.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PasswordChangeRequest {
    @NotEmpty
    @Size(min = 6, max = 12)
    private String newPassword;
}
