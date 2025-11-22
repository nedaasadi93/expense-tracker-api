package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.AuthResponse;
import com.example.expensetracker.auth.dto.LoginWithPasswordRequest;
import com.example.expensetracker.auth.dto.RegisterRequest;
import com.example.expensetracker.auth.dto.TokenResponse;
import com.example.expensetracker.security.jwt.JwtService;
import com.example.expensetracker.security.jwt.JwtTokenType;
import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    public AuthResponse loginWithPassword(LoginWithPasswordRequest request) {
        UserEntity user = userService.findByMobile(request.getMobile())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isVerified()) {
            throw new RuntimeException("verify your mobile number before logging in");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        TokenResponse tokens = generateTokens(user.getId());
        return AuthResponse.builder()
                .user(userService.toResponse(user))
                .tokens(tokens)
                .build();
    }

    private TokenResponse generateTokens(Long userId) {
        String accessToken = jwtService.generateToken(userId, JwtTokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(userId, JwtTokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
