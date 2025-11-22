package com.example.expensetracker.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${security.jwt.access.secret}")
    private String accessSecret;

    @Value("${security.jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${security.jwt.access.expiration}")
    private long accessExpiration;

    @Value("${security.jwt.refresh.expiration}")
    private long refreshExpiration;


    public void validateToken(String token, JwtTokenType accessToken) {
    }

    public long extractUserId(String token, JwtTokenType accessToken) {
        return 0;
    }
}
