package com.example.expensetracker.category.service;

import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    @Override
    public Page<CategoryResponse> getAll(CategoryFilter filter) {
        return null;
    }
}
