package com.example.juniorjavadeveloperbvpsoftware.controller;

import com.example.juniorjavadeveloperbvpsoftware.dto.request.UserRequest;
import com.example.juniorjavadeveloperbvpsoftware.dto.response.LoginResponse;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.service.AuthenticationService;
import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationService authenticationService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        userService.saveUser(user);
        return new ResponseEntity<>(String.format("You have been successfully registered as a user. Please check your email %s and confirm your registration.", user.getEmail()),
                HttpStatus.CREATED);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserRequest userRequest) {
        return new ResponseEntity<>(authenticationService
                .login(userService.findByEmail(userRequest.getEmail()),
                        userRequest.getPassword()),
                HttpStatus.OK);
    }
}
