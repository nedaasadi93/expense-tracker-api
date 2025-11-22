package com.example.expensetracker.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

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


    private Key getSigningKey(JwtTokenType type) {
        return Keys.hmacShaKeyFor((type == JwtTokenType.ACCESS_TOKEN ? accessSecret : refreshSecret).getBytes());
    }

    private long expiry(JwtTokenType type) {
        return type == JwtTokenType.ACCESS_TOKEN ? accessExpiration : refreshExpiration;
    }

    public String generateToken(Long userId, JwtTokenType type) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiry(type)))
                .claim("token_type", type.name())
                .signWith(getSigningKey(type), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token, JwtTokenType type) {
        Boolean expired = isTokenExpired(token, type);
        if (expired) {
            throw new RuntimeException("token expired");
        }
    }

    private Boolean isTokenExpired(String token, JwtTokenType type) {
        return parseClaims(token, type).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token, JwtTokenType type) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(type))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("invalid token");
        }
    }

    public String extractUserId(String token, JwtTokenType type) {
        return parseClaims(token, type).getSubject();
    }
}
