package com.example.expensetracker.auth.dto.otp;

import com.example.expensetracker.auth.statics.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank(message = "MOBILE_MUST_NOT_BE_NULL")
    @Pattern(regexp = Regex.MOBILE, message = "MOBILE_LENGTH")
    private String mobile;

    @NotBlank(message = "OTP_MUST_NOT_BE_NULL")
    private String otp;
}
