package com.example.juniorjavadeveloperbvpsoftware.service;

import com.example.juniorjavadeveloperbvpsoftware.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.SecureRandom;
import java.util.List;

public interface UserService extends UserDetailsService {
    User saveUser(User user);
    User readById(long id);
    User updateUser(User user);
    User findByEmail(String email);
    UserDetails loadUserWithoutPassword(String email);
    void delete(long id);
    List<User> getAllUsers();
}
