package com.example.expensetracker.category.mapper;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryRequest;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.dto.CategoryUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
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

    @Override
    public List<CategoryResponse> toResponseList(List<CategoryEntity> categories) {
        return categories.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void updateEntity(CategoryUpdateRequest request, CategoryEntity entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setMonthlyLimit(request.getMonthlyLimit());
    }

    @Override
    public CategoryEntity createCategory(CategoryRequest request, Long userId) {
        return CategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .userId(userId)
                .monthlyLimit(request.getMonthlyLimit())
                .build();
    }
}
