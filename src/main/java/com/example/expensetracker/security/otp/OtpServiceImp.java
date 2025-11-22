package com.example.expensetracker.security.otp;

import com.example.expensetracker.auth.dto.otp.OtpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImp implements OtpService  {
    @Override
    public OtpResponse generateOtp(String key) {
        return null;
    }
}
