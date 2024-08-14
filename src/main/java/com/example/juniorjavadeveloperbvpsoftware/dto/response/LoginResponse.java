package com.example.juniorjavadeveloperbvpsoftware.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    String email;
    String token;
    String refreshToken;
}
