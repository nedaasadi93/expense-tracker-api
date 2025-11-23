package com.example.expensetracker.expense.dto;

import com.example.expensetracker.category.dto.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private CategoryResponse category;
}
