package com.example.expensetracker.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CategoryUpdateRequest {
    @NotNull(message = "CATEGORY_NAME_MUST_NOT_BE_NULL")
    private String name;
    private String description;
    @NotNull(message = "LIMIT_AMOUNT_MUST_BE_NOT_BE_NULL")
    @Positive(message = "LIMIT_AMOUNT_MUST_BE_GREATER_THAN_ZERO")
    private BigDecimal monthlyLimit;
}
