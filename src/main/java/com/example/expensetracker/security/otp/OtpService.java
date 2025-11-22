package com.example.expensetracker.security.otp;


import com.example.expensetracker.auth.dto.otp.OtpResponse;

public interface OtpService {
    OtpResponse generateOtp(String key);

    boolean validateOtp(String mobile, String inputCode);
}
