package com.example.juniorjavadeveloperbvpsoftware.controller;

import com.example.juniorjavadeveloperbvpsoftware.dto.response.UserResponse;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/api/user/current-user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public UserDetails infoAboutCurrentUser(Authentication authentication) {
        return userService.loadUserWithoutPassword(authentication.getName());
    }

    @GetMapping("/api/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            users.add(modelMapper.map(user, UserResponse.class));
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
