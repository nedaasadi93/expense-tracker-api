package com.example.expensetracker.auth.dto.otp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtpResponse {
    private boolean sendOtp;
    private String code;

    public OtpResponse(boolean sendOtp, String code) {
        this.sendOtp = sendOtp;
        this.code = code;
    }
}
