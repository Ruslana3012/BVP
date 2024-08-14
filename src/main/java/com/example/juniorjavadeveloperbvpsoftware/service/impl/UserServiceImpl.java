package com.example.juniorjavadeveloperbvpsoftware.service.impl;

import com.example.juniorjavadeveloperbvpsoftware.exception.NullEntityReferenceException;
import com.example.juniorjavadeveloperbvpsoftware.exception.UserAlreadyExistsException;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.repository.RoleRepository;
import com.example.juniorjavadeveloperbvpsoftware.repository.UserRepository;
import com.example.juniorjavadeveloperbvpsoftware.service.EmailService;
import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    @Override
    public User saveUser(User user) {
        if (user == null) {
            log.error("Attempted to save null user");
            throw new NullEntityReferenceException("User cannot be 'null'");
        } else if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            log.error("Attempted to save empty email or password");
            throw new NullPointerException("Attempted to save empty email or password");
        } else if (userRepository.findByEmail(user.getEmail()) != null) {
            log.error("Attempted to save user with existing email: {}", user.getEmail());
            throw new UserAlreadyExistsException("Attempted to save user with existing email");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(roleRepository.findByName("USER"));
            user.setUrlForVerifiedEmail(UUID.randomUUID().toString());
            userRepository.save(user);

            emailService.sendEmailConfirmation(user.getEmail(), user.getUrlForVerifiedEmail());
            log.info("User saved: {}", user.getEmail());
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            log.error("Attempted to update null user");
            throw new NullEntityReferenceException("User cannot be 'null'");
        } else if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            log.error("Attempted to update empty email or password");
            throw new NullPointerException("Attempted to update empty email or password");
        } else {
            return userRepository.save(user);
        }
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("Unable to find user with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public void delete(long id) {
        userRepository.delete(readById(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not Found!");
        }
        return user;
    }

    public UserDetails loadUserWithoutPassword(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not Found!");
        }
        user.setPassword("********");
        return user;

    }
}
