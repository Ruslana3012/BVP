package com.example.juniorjavadeveloperbvpsoftware.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DBConfig {
    private String email;

    @Autowired
    public void setEmail(@Value("${spring.mail.username}") String email) {
        this.email = email;
    }
}
