package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.dto.response.LoginResponse;
import com.example.juniorjavadeveloperbvpsoftware.model.User;

public interface AuthenticationService {
    LoginResponse login(User user, String password);

}
