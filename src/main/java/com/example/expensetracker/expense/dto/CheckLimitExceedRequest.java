package com.example.expensetracker.expense.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CheckLimitExceedRequest {
    @NotNull
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotNull
    private LocalDateTime expenseDate;
}
