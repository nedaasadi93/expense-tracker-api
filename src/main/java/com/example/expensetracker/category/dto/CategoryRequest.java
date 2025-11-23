package com.example.expensetracker.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryRequest {
    @NotNull
    private String name;
    private String description;
    @NotNull
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal monthlyLimit;
}
