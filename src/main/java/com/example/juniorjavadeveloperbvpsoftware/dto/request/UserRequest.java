package com.example.juniorjavadeveloperbvpsoftware.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRequest {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

    public String getLogin() {
        return email;
    }
}
