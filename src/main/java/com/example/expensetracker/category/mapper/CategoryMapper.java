package com.example.expensetracker.category.mapper;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.dto.CategoryUpdateRequest;

import java.util.List;


public interface CategoryMapper {

    CategoryResponse toResponse(CategoryEntity category);

    List<CategoryResponse> toResponseList(List<CategoryEntity> categories);

    void updateEntity(CategoryUpdateRequest request, CategoryEntity entity);

}
