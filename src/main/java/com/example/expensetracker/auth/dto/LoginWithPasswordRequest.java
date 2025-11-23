package com.example.expensetracker.auth.dto;

import com.example.expensetracker.auth.statics.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginWithPasswordRequest {
    @NotBlank(message = "MOBILE_MUST_NOT_BE_NULL")
    @Pattern(regexp = Regex.MOBILE, message = "MOBILE_LENGTH")
    private String mobile;

    @NotBlank(message = "PASSWORD_MUST_NOT_BE_NULL")
    private String password;
}
