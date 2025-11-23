package com.example.expensetracker.auth.controller;

import com.example.expensetracker.auth.dto.*;
import com.example.expensetracker.auth.dto.otp.OtpRequest;
import com.example.expensetracker.auth.dto.otp.VerifyOtpRequest;
import com.example.expensetracker.auth.service.AuthService;
import com.example.expensetracker.auth.statics.AuthRestApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Authentication rest controllers")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register", description = "Registers a new user and grants access to the /idn endpoints")
    @PostMapping(path = AuthRestApi.REGISTER)
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Login", description = "Authenticates the user and returns an access token, refresh token (both JWT), and user details")
    @PostMapping(path = AuthRestApi.LOGIN)
    public ResponseEntity<AuthResponse> loginWithPassword(@Valid @RequestBody LoginWithPasswordRequest request) {
        return ResponseEntity.ok(authService.loginWithPassword(request));
    }

    @Operation(summary = "Refresh", description = "Generates a new access token and refresh token (both JWT)")
    @PostMapping(path = AuthRestApi.REFRESH)
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @Operation(summary = "Request OTP", description = "Sends a one-time password (OTP) to the user for account verification")
    @PostMapping(path = AuthRestApi.REQUEST_OTP)
    public ResponseEntity<Void> requestOtp(@Valid @RequestBody OtpRequest request) {
        authService.requestOtp(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify OTP", description = "Verifies the user account using the provided OTP")
    @PostMapping(path = AuthRestApi.VERIFY_OTP)
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }
}
