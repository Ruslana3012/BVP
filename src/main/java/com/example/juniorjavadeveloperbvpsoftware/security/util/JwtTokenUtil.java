package com.example.juniorjavadeveloperbvpsoftware.security.util;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtTokenUtil {

    public static byte[] encodeSecretKey(String secretKey) {
        return Base64.getEncoder()
                .encodeToString(secretKey.getBytes())
                .getBytes();
    }
}
