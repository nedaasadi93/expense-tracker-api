package com.example.expensetracker.expense.service;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.service.CategoryService;
import com.example.expensetracker.common.exception.BadRequestException;
import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.common.util.DateUtil;
import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.dto.ExpenseUpdateRequest;
import com.example.expensetracker.expense.mapper.ExpenseMapper;
import com.example.expensetracker.expense.repository.ExpenseRepository;
import com.example.expensetracker.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImp implements ExpenseService{

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getById(Long id, Long categoryId) {
        ExpenseEntity expense = findExpenseAndValidateCategory(id, categoryId);
        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseResponse create(ExpenseRequest request, Long categoryId) {
        Long userId = getCurrentUserId();
        validateExpense(request.getExpenseDate(), categoryId, userId, request.getAmount());
        ExpenseEntity expense = expenseMapper.createExpense(request, userId, categoryId);
        expenseRepository.save(expense);
        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseResponse update(Long id, ExpenseUpdateRequest request, Long categoryId) {
        Long userId = getCurrentUserId();
        ExpenseEntity expense = findExpenseAndValidateCategory(id, categoryId);
        validateExpense(request.getExpenseDate(), categoryId, userId, request.getAmount());
        expenseMapper.updateEntity(request, expense);
        expenseRepository.save(expense);
        return expenseMapper.toResponse(expense);
    }


    private void validateExpense(LocalDateTime expenseDate, Long categoryId, Long userId, BigDecimal amount) {
        validateExpenseDate(expenseDate);
        CategoryEntity category = categoryService.findByIdAndUserIdOrThrowException(categoryId, userId);
        validateMonthlyLimit(userId, expenseDate, amount, category);
    }

    private void validateExpenseDate(LocalDateTime date) {
        if (date.isAfter(LocalDateTime.now()))
            throw new BadRequestException(ExceptionModel
                    .builder()
                    .errorCode(ErrorCodes.EXPENSE_DATE_CAN_NOT_BE_IN_FUTURE.getCode())
                    .messageKey(ErrorCodes.EXPENSE_DATE_CAN_NOT_BE_IN_FUTURE.getMessage())
                    .build());
    }

    private void validateMonthlyLimit(Long userId, LocalDateTime expenseDate, BigDecimal amount, CategoryEntity category) {
        LocalDateTime start = DateUtil.startOfMonth(expenseDate);
        LocalDateTime end = DateUtil.endOfMonth(expenseDate);

        BigDecimal spentAmount = expenseRepository.totalSpentInCategory(userId, category.getId(), start, end);
        if (spentAmount == null) {
            spentAmount = BigDecimal.ZERO;
        }

        if (spentAmount.add(amount).compareTo(category.getMonthlyLimit()) > 0)
            throw new BadRequestException(ExceptionModel
                    .builder()
                    .errorCode(ErrorCodes.MONTHLY_LIMIT_EXCEEDED_FOR_CATEGORY.getCode())
                    .messageKey(ErrorCodes.MONTHLY_LIMIT_EXCEEDED_FOR_CATEGORY.getMessage())
                    .build());
    }

    private ExpenseEntity findExpenseAndValidateCategory(Long id, Long categoryId) {
        ExpenseEntity expense = findByIdAndUserIdOrThrowException(id, getCurrentUserId());
        checkExpenseBelongToCategory(expense.getCategoryId(), categoryId);
        return expense;
    }

    private ExpenseEntity findByIdAndUserIdOrThrowException(Long id, Long userId) {
        return expenseRepository.findByIdAndUserIdWithCategory(id, userId)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.EXPENSE_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.EXPENSE_NOT_FOUND.getMessage())
                                .build()));
    }

    private void checkExpenseBelongToCategory(Long categoryId, Long urlCategoryId) {
        if (!categoryId.equals(urlCategoryId))
            throw new BadRequestException(ExceptionModel
                    .builder()
                    .errorCode(ErrorCodes.EXPENSE_CATEGORY_MISMATCH.getCode())
                    .messageKey(ErrorCodes.EXPENSE_CATEGORY_MISMATCH.getMessage())
                    .build());
    }

    private Long getCurrentUserId() {
        return JwtUser.getAuthenticatedUser().getId();
    }
}
