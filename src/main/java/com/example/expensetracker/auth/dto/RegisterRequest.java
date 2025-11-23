package com.example.expensetracker.auth.dto;

import com.example.expensetracker.auth.statics.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "MOBILE_MUST_NOT_BE_NULL")
    @Pattern(regexp = Regex.MOBILE, message = "MOBILE_LENGTH")
    private String mobile;

    @NotBlank(message = "PASSWORD_MUST_NOT_BE_NULL")
    @Size(min = 6, max = 20, message = "PASSWORD_SIZE")
    private String password;

    private String name;
}
