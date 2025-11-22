package com.example.expensetracker.security.jwt;

import lombok.Getter;

@Getter
public enum JwtTokenType {
    ACCESS_TOKEN,
    REFRESH_TOKEN
}
