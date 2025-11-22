package com.example.expensetracker.category.controller;

import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.service.CategoryService;
import com.example.expensetracker.category.statics.CategoryRestApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(path = CategoryRestApi.CATEGORIES)
    public ResponseEntity<Page<CategoryResponse>> getAll(@Valid CategoryFilter filter) {
        return ResponseEntity.ok(categoryService.getAll(filter));
    }
}
