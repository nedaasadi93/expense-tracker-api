package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.RegisterRequest;
import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userService.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile already registered");
        }
        UserEntity user = UserEntity.createNewUser(
                request.getMobile(),
                passwordEncoder.encode(request.getPassword()),
                request.getName());
        userService.save(user);

    }
}
