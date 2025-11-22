package com.example.expensetracker.auth.dto.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number has exactly 11 digits")
    private String mobile;

    @NotBlank
    private String otp;
}
