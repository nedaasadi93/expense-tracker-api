package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.*;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse loginWithPassword(LoginWithPasswordRequest request);

    TokenResponse refresh(RefreshRequest request);
}
