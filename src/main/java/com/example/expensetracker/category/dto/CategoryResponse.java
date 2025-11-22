package com.example.expensetracker.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse implements Serializable {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private BigDecimal monthlyLimit;
}
