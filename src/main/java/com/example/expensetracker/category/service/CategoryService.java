package com.example.expensetracker.category.service;

import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<CategoryResponse> getAll(CategoryFilter filter);
}
