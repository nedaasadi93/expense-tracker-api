package com.example.expensetracker.auth.dto;

import com.example.expensetracker.user.dto.UserResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private UserResponse user;
    private TokenResponse tokens;
}
