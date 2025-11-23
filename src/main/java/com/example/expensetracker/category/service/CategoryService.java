package com.example.expensetracker.category.service;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryRequest;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.dto.CategoryUpdateRequest;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<CategoryResponse> getAll(CategoryFilter filter);

    CategoryResponse getById(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryUpdateRequest request);

    boolean delete(Long id);

    CategoryEntity findByIdAndUserIdOrThrowException(Long id, Long userId);
}
