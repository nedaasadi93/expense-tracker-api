package com.example.expensetracker.category.service;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryRequest;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.dto.CategoryUpdateRequest;
import com.example.expensetracker.category.mapper.CategoryMapper;
import com.example.expensetracker.category.repository.CategoryRepository;
import com.example.expensetracker.category.specification.CategorySpecification;
import com.example.expensetracker.common.exception.ConflictException;
import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
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


    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        Long userId = getCurrentUserId();
        CategoryEntity category = findByIdAndUserIdOrThrowException(id, userId);
        return categoryMapper.toResponse(category);
    }

    public CategoryEntity findByIdAndUserIdOrThrowException(Long id, Long userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.CATEGORY_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.CATEGORY_NOT_FOUND.getMessage())
                                .build()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponse create(CategoryRequest request) {
        Long userId = getCurrentUserId();
        validateCategoryNameUniqueness(userId, request.getName());

        CategoryEntity category = CategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .userId(userId)
                .monthlyLimit(request.getMonthlyLimit())
                .build();

        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
        Long userId = getCurrentUserId();
        CategoryEntity category = findByIdAndUserIdOrThrowException(id, userId);
        categoryMapper.updateEntity(request, category);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        Long userId = getCurrentUserId();
        CategoryEntity category = findByIdAndUserIdOrThrowException(id, userId);
        categoryRepository.delete(category);
        return true;
    }


    private void validateCategoryNameUniqueness(Long userId, String name) {
        if (categoryRepository.existsByUserIdAndName(userId, name)) {
            throw new ConflictException(
                    ExceptionModel
                            .builder()
                            .errorCode(ErrorCodes.DUPLICATE_CATEGORY.getCode())
                            .messageKey(ErrorCodes.DUPLICATE_CATEGORY.getMessage())
                            .build());
        }
    }


    private Long getCurrentUserId() {
        return JwtUser.getAuthenticatedUser().getId();
    }

}
