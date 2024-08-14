package com.example.juniorjavadeveloperbvpsoftware.repository;

import com.example.juniorjavadeveloperbvpsoftware.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUrlForVerifiedEmail(String url);
}
