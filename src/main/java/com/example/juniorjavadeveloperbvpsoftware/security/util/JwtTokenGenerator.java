package com.example.juniorjavadeveloperbvpsoftware.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;

import static com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenUtil.encodeSecretKey;

@Component
public class JwtTokenGenerator {

    private final String secretKey;
    private final long tokenExpireMilliseconds;
    private final long refreshExpireMilliseconds;
    private final Clock clock;

    public JwtTokenGenerator(String secretKey, long tokenExpireMilliseconds, long refreshExpireMilliseconds, Clock clock
    ) {
        this.secretKey = secretKey;
        this.tokenExpireMilliseconds = tokenExpireMilliseconds;
        this.refreshExpireMilliseconds = refreshExpireMilliseconds;
        this.clock = clock;
    }

    public String generateToken(String username) {
        return generateTokenWithExpiry(username, tokenExpireMilliseconds);
    }

    public String generateRefreshToken(String username) {
        return generateTokenWithExpiry(username, refreshExpireMilliseconds);
    }

    public String generateTokenWithExpiry(String username, long expiry) {
        Date expireDate = new Date(clock.millis() + expiry);

        return Jwts.builder()
                .subject(username)
                .expiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(encodeSecretKey(secretKey)))
                .compact();
    }
}
