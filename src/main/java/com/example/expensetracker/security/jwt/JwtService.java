package com.example.expensetracker.security.jwt;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public void validateToken(String token, JwtTokenType accessToken) {
    }

    public long extractUserId(String token, JwtTokenType accessToken) {
        return 0;
    }
}
