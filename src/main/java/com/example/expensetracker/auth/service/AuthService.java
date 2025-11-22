package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);
}
