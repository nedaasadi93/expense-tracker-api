package com.example.expensetracker.auth.controller;

import com.example.expensetracker.auth.dto.*;
import com.example.expensetracker.auth.service.AuthService;
import com.example.expensetracker.auth.statics.AuthRestApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = AuthRestApi.REGISTER)
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }


    @PostMapping(path = AuthRestApi.LOGIN)
    public ResponseEntity<AuthResponse> loginWithPassword(@Valid @RequestBody LoginWithPasswordRequest request) {
        return ResponseEntity.ok(authService.loginWithPassword(request));
    }


    @PostMapping(path = AuthRestApi.REFRESH)
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
