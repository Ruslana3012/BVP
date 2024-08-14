package com.example.juniorjavadeveloperbvpsoftware.repository;

import com.example.juniorjavadeveloperbvpsoftware.model.ResetPassword;
import com.example.juniorjavadeveloperbvpsoftware.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    ResetPassword findByEmail(String email);
    ResetPassword findByVerificationToken(String token);
}