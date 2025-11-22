package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.*;
import com.example.expensetracker.auth.dto.otp.OtpRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse loginWithPassword(LoginWithPasswordRequest request);

    TokenResponse refresh(RefreshRequest request);

    void requestOtp(OtpRequest request);
}
