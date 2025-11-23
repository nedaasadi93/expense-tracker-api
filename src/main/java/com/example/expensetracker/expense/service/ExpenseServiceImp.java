package com.example.expensetracker.expense.service;

import com.example.expensetracker.common.exception.BadRequestException;
import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.mapper.ExpenseMapper;
import com.example.expensetracker.expense.repository.ExpenseRepository;
import com.example.expensetracker.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImp implements ExpenseService{

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getById(Long id, Long categoryId) {
        ExpenseEntity expense = findExpenseAndValidateCategory(id, categoryId);
        return expenseMapper.toResponse(expense);
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
