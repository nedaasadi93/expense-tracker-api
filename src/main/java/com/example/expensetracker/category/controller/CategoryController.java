package com.example.expensetracker.category.controller;

import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryRequest;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.dto.CategoryUpdateRequest;
import com.example.expensetracker.category.service.CategoryService;
import com.example.expensetracker.category.statics.CategoryRestApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Category", description = "Category rest controllers")
@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get all", description = "Returns all categories owned by the authenticated user")
    @GetMapping(path = CategoryRestApi.CATEGORIES)
    public ResponseEntity<Page<CategoryResponse>> getAll(@Valid CategoryFilter filter) {
        return ResponseEntity.ok(categoryService.getAll(filter));
    }

    @Operation(summary = "Load by id", description = "Returns a category by its ID if it is owned by the authenticated user")
    @GetMapping(path = CategoryRestApi.CATEGORIES_ID)
    public ResponseEntity<CategoryResponse> getById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Operation(summary = "Create", description = "Creates a new category for the authenticated user")
    @PostMapping(path = CategoryRestApi.CATEGORIES)
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @Operation(summary = "Update", description = "Updates a category by its ID if it is owned by authenticated user")
    @PutMapping(path = CategoryRestApi.CATEGORIES_ID)
    public ResponseEntity<CategoryResponse> update(@PathVariable(value = "id") Long id, @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @Operation(summary = "Delete", description = "Delete a category by its ID if it is owned by authenticated user")
    @DeleteMapping(path = CategoryRestApi.CATEGORIES_ID)
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
