package com.example.expensetracker.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Mobile number has exactly 11 digits")
    private String mobile;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    private String name;
}
