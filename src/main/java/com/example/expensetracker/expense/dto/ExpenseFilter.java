package com.example.expensetracker.expense.dto;

import com.example.expensetracker.common.dto.PageableFilter;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseFilter extends PageableFilter {
    private String name;
    private String description;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private Long userId;
    private Long categoryId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public void putUserId(Long userId) {
        this.userId = userId;
    }
    public void putCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
