package com.example.expensetracker.auth.dto.otp;

import com.example.expensetracker.auth.statics.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank
    @Pattern(regexp = Regex.MOBILE, message = "Mobile number has exactly 11 digits")
    private String mobile;

    @NotBlank
    private String otp;
}
