package com.example.juniorjavadeveloperbvpsoftware.config;

import com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ConfigProperties {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-milliseconds}")
    private long tokenExpireMilliseconds;
    @Value("${security.jwt.refresh-token.expire-milliseconds}")
    private long refreshTokenExpireMilliseconds;

    @Bean
    public String getSecretKey() {
        return secretKey;
    }

    @Bean
    public long getTokenExpireMilliseconds() {
        return tokenExpireMilliseconds;
    }

    @Bean
    public long getRefreshTokenExpireMilliseconds() {
        return refreshTokenExpireMilliseconds;
    }

    @Bean
    public JwtTokenGenerator jwtTokenGenerator(ConfigProperties configProperties) {
        return new JwtTokenGenerator(configProperties.getSecretKey(),
                configProperties.getTokenExpireMilliseconds(),
                configProperties.getRefreshTokenExpireMilliseconds(),
                Clock.systemDefaultZone());
    }
}
