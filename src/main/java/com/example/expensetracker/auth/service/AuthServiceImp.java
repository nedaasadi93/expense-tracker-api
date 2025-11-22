package com.example.expensetracker.auth.service;

import com.example.expensetracker.auth.dto.*;
import com.example.expensetracker.auth.dto.otp.OtpRequest;
import com.example.expensetracker.auth.dto.otp.VerifyOtpRequest;
import com.example.expensetracker.common.exception.*;
import com.example.expensetracker.security.jwt.JwtService;
import com.example.expensetracker.security.jwt.JwtTokenType;
import com.example.expensetracker.security.otp.OtpService;
import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        if (userService.existsByMobile(request.getMobile())) {
            throw new ConflictException(
                    ExceptionModel
                            .builder()
                            .errorCode(ErrorCodes.DUPLICATE_MOBILE_NUMBER.getCode())
                            .messageKey(ErrorCodes.DUPLICATE_MOBILE_NUMBER.getMessage())
                            .build());
        }
        UserEntity user = UserEntity.createNewUser(
                request.getMobile(),
                passwordEncoder.encode(request.getPassword()),
                request.getName());
        userService.save(user);
        otpService.generateOtp(request.getMobile());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse loginWithPassword(LoginWithPasswordRequest request) {
        UserEntity user = userService.findByMobile(request.getMobile())
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.USER_NOT_FOUND.getMessage())
                                .build()));
        if (!user.isVerified()) {
            throw new UserMobileUnverifiedException(
                    ExceptionModel
                            .builder()
                            .errorCode(ErrorCodes.USER_IS_NOT_VERIFIED.getCode())
                            .messageKey(ErrorCodes.USER_IS_NOT_VERIFIED.getMessage())
                            .build());
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(
                    ExceptionModel.builder()
                            .errorCode(ErrorCodes.INVALID_PASSWORD.getCode())
                            .messageKey(ErrorCodes.INVALID_PASSWORD.getMessage())
                            .build());
        }
        TokenResponse tokens = generateTokens(user.getId());
        return AuthResponse.builder()
                .user(userService.toResponse(user))
                .tokens(tokens)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse refresh(RefreshRequest request) {
        Long userId = Long.valueOf(jwtService.extractUserId(request.getRefreshToken(), JwtTokenType.REFRESH_TOKEN));
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.USER_NOT_FOUND.getMessage())
                                .build()));
        jwtService.validateToken(request.getRefreshToken(), JwtTokenType.REFRESH_TOKEN);
        return generateTokens(user.getId());
    }

    private TokenResponse generateTokens(Long userId) {
        String accessToken = jwtService.generateToken(userId, JwtTokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(userId, JwtTokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requestOtp(OtpRequest request) {
        UserEntity user = userService.findByMobile(request.getMobile())
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.USER_NOT_FOUND.getMessage())
                                .build()));
        otpService.generateOtp(user.getMobile());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        UserEntity user = userService.findByMobile(request.getMobile())
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.USER_NOT_FOUND.getMessage())
                                .build()));

        if (!otpService.validateOtp(request.getMobile(), request.getOtp()))
            throw new InvalidOtpException(
                    ExceptionModel
                            .builder()
                            .messageKey(ErrorCodes.INVALID_OTP.getMessage())
                            .errorCode(ErrorCodes.INVALID_OTP.getCode())
                            .build());

        if (!user.isVerified()) {
            user.verified();
            userService.save(user);
        }
        TokenResponse tokens = generateTokens(user.getId());
        return AuthResponse.builder()
                .user(userService.toResponse(user))
                .tokens(tokens)
                .build();
    }
}
