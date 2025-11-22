package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.AuthResponse;
import com.example.expensetracker.auth.dto.LoginWithPasswordRequest;
import com.example.expensetracker.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse loginWithPassword(LoginWithPasswordRequest request);
}
