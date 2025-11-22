package com.example.expensetracker.category.mapper;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
    public CategoryResponse toResponse(CategoryEntity category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .userId(category.getUserId())
                .monthlyLimit(category.getMonthlyLimit())
                .build();
    }

    public List<CategoryResponse> toResponseList(List<CategoryEntity> categories) {
        return categories.stream()
                .map(this::toResponse)
                .toList();
    }

}
