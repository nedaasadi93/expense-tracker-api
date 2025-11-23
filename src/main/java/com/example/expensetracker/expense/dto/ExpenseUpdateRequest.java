package com.example.expensetracker.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseUpdateRequest {
    @NotBlank(message = "EXPENSE_NAME_MUST_NOT_BE_NULL")
    private String name;
    private String description;
    @NotNull(message = "EXPENSE_AMOUNT_MUST_NOT_BE_NULL")
    @Positive(message = "EXPENSE_AMOUNT_MUST_BE_GREATER_THAN_ZERO")
    private BigDecimal amount;
    @NotNull(message = "EXPENSE_DATE_MUST_NOT_BE_NULL")
    private LocalDateTime expenseDate;
}
