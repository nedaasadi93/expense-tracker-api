package com.example.expensetracker.security.jwt;

import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.UnauthorizedException;
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
            throw new UnauthorizedException(
                    ExceptionModel.builder()
                            .errorCode(ErrorCodes.TOKEN_EXPIRED.getCode())
                            .messageKey(ErrorCodes.TOKEN_EXPIRED.getMessage())
                            .build());
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
            throw new UnauthorizedException(
                    ExceptionModel.builder()
                            .errorCode(ErrorCodes.INVALID_TOKEN.getCode())
                            .messageKey(ErrorCodes.INVALID_TOKEN.getMessage())
                            .build());
        }
    }

    public String extractUserId(String token, JwtTokenType type) {
        return parseClaims(token, type).getSubject();
    }
}
