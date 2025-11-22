package com.example.expensetracker.category.service;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.mapper.CategoryMapper;
import com.example.expensetracker.category.repository.CategoryRepository;
import com.example.expensetracker.category.specification.CategorySpecification;
import com.example.expensetracker.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAll(CategoryFilter filter) {
        Long userId = getCurrentUserId();
        filter.putUserId(userId);
        Specification<CategoryEntity> spec = CategorySpecification.filter(filter);
        return categoryRepository
                .findAll(spec, filter.toPageable())
                .map(categoryMapper::toResponse);
    }

    private Long getCurrentUserId() {
        return JwtUser.getAuthenticatedUser().getId();
    }
}
