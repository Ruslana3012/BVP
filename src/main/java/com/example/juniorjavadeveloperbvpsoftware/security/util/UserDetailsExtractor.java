package com.example.juniorjavadeveloperbvpsoftware.security.util;

import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenUtil.encodeSecretKey;

@Component
public class UserDetailsExtractor {

    private final String secretKey;
    private final UserService userDetails;

    public UserDetailsExtractor(String secretKey, UserService userDetails) {
        this.secretKey = secretKey;
        this.userDetails = userDetails;
    }

    public Optional<UserDetails> extractFromToken(String token) {
        try {
            var claimsJws = parseToken(token);
            var username = claimsJws.getPayload().getSubject();
            return Optional.ofNullable(userDetails.loadUserByUsername(username));
        } catch (Exception e) {
            throw new BadCredentialsException("Expired or invalid JWT token");
        }
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                //         .verifyWith(Keys.hmacShaKeyFor(encodeSecretKey(secretKey)))
                .setSigningKey(encodeSecretKey(secretKey))
                .build()
                .parseSignedClaims(token);
    }
}