package com.example.expensetracker.security.otp;

import com.example.expensetracker.auth.dto.otp.OtpResponse;
import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.TooManyRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImp implements OtpService  {

    @Value("${otp.duration}")
    private Integer otpDuration;

    @Value("${otp.max-attempts}")
    private Integer maxAttempts;

    @Value("${otp.max-request}")
    private Integer maxRequests;

    private final RedisTemplate<String, String> redis;

    private static final String OTP_CODE = "OTP_CODE:";
    private static final String OTP_ATTEMPTS = "OTP_ATTEMPTS:";
    private static final String OTP_REQUEST = "OTP_REQUEST:";

    @Override
    public OtpResponse generateOtp(String mobile) {
        String reqKey = OTP_REQUEST + mobile;
        ValueOperations<String, String> ops = redis.opsForValue();

        Long reqCount = ops.increment(reqKey);
        if (reqCount != null && reqCount == 1L) {
            redis.expire(reqKey, 60, TimeUnit.SECONDS);
        }

        if (reqCount != null && reqCount > maxRequests) {
            throw new TooManyRequestException(
                    ExceptionModel
                            .builder()
                            .errorCode(ErrorCodes.TOO_MANY_OTP_REQUEST.getCode())
                            .messageKey(ErrorCodes.TOO_MANY_OTP_REQUEST.getMessage())
                            .build());
        }

        String otpKey = OTP_CODE + mobile;
        String otpAttemptsKey = OTP_ATTEMPTS + mobile;
        String existingCode = ops.get(otpKey);

        if (existingCode != null) {
            return new OtpResponse(true, existingCode);
        }

        String code = generateNewOtp();
        ops.set(otpKey, code, otpDuration, TimeUnit.SECONDS);
        ops.set(otpAttemptsKey, "0", otpDuration, TimeUnit.SECONDS);

        return new OtpResponse(true, code);
    }

    private String generateNewOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
    }

}
