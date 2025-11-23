package com.example.expensetracker.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseUpdateRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotNull
    private LocalDateTime expenseDate;
}
