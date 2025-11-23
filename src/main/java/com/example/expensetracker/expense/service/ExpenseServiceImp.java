package com.example.expensetracker.expense.service;

import com.example.expensetracker.alert.AlertResponse;
import com.example.expensetracker.alert.AlertType;
import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.service.CategoryService;
import com.example.expensetracker.common.exception.BadRequestException;
import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.common.util.DateUtil;
import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.*;
import com.example.expensetracker.expense.mapper.ExpenseMapper;
import com.example.expensetracker.expense.repository.ExpenseRepository;
import com.example.expensetracker.expense.specification.ExpenseSpecification;
import com.example.expensetracker.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getAll(ExpenseFilter filter) {
        filter.putUserId(getCurrentUserId());
        Specification<ExpenseEntity> spec = ExpenseSpecification.filter(filter);
        return expenseRepository.findAll(spec, filter.toPageable())
                .map(expenseMapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseResponse create(ExpenseRequest request, Long categoryId) {
        Long userId = getCurrentUserId();
        validateExpense(request.getExpenseDate(), categoryId, userId);
        ExpenseEntity expense = createExpense(request, userId, categoryId);
        expenseRepository.save(expense);
        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpenseResponse update(Long id, ExpenseUpdateRequest request, Long categoryId) {
        Long userId = getCurrentUserId();
        ExpenseEntity expense = findExpenseAndValidateCategory(id, categoryId);
        validateExpense(request.getExpenseDate(), categoryId, userId);
        expenseMapper.updateEntity(request, expense);
        expenseRepository.save(expense);
        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id, Long categoryId) {
        ExpenseEntity expense = findExpenseAndValidateCategory(id, categoryId);
        expenseRepository.delete(expense);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getMonthlyExpenses(Long categoryId, int year, int month) {
        Long userId = getCurrentUserId();
        LocalDateTime start = LocalDateTime.from(LocalDate.of(year, month, 1));
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        List<ExpenseEntity> expenses = expenseRepository.findByUserIdAndCategoryIdAndExpenseDateBetween(userId, categoryId, start, end);
        return expenseMapper.toResponseList(expenses);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponse checkLimitExceed(Long categoryId, CheckLimitExceedRequest request) {

        Long userId = getCurrentUserId();
        CategoryEntity category = categoryService.findByIdAndUserIdOrThrowException(categoryId, userId);

        LocalDateTime start = DateUtil.startOfMonth(request.getExpenseDate());
        LocalDateTime end = DateUtil.endOfMonth(request.getExpenseDate());

        BigDecimal spentAmount = expenseRepository.totalSpentInCategory(userId, category.getId(), start, end);
        if (spentAmount == null) {
            spentAmount = BigDecimal.ZERO;
        }

        if (spentAmount.add(request.getAmount()).compareTo(category.getMonthlyLimit()) > 0) {
            return AlertResponse.builder()
                    .type(AlertType.LIMIT_EXCEEDED)
                    .message("You will exceed your monthly limit for category: " + category.getName())
                    .currentAmount(request.getAmount())
                    .monthlyLimit(category.getMonthlyLimit())
                    .build();
        }

        return  AlertResponse.builder()
                .type(AlertType.WITHIN_LIMIT)
                .message("You are within your monthly limit for category: " + category.getName())
                .currentAmount(request.getAmount())
                .monthlyLimit(category.getMonthlyLimit())
                .build();
    }


    private void validateExpense(LocalDateTime expenseDate, Long categoryId, Long userId) {
        validateExpenseDate(expenseDate);
        CategoryEntity category = categoryService.findByIdAndUserIdOrThrowException(categoryId, userId);
    }

    private void validateExpenseDate(LocalDateTime date) {
        if (date.isAfter(LocalDateTime.now()))
            throw new BadRequestException(ExceptionModel
                    .builder()
                    .errorCode(ErrorCodes.EXPENSE_DATE_CAN_NOT_BE_IN_FUTURE.getCode())
                    .messageKey(ErrorCodes.EXPENSE_DATE_CAN_NOT_BE_IN_FUTURE.getMessage())
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

    public ExpenseEntity createExpense(ExpenseRequest request, Long userId, Long categoryId) {
        return ExpenseEntity.builder()
                .userId(userId)
                .categoryId(categoryId)
                .amount(request.getAmount())
                .description(request.getDescription())
                .expenseDate(request.getExpenseDate())
                .name(request.getName())
                .build();
    }

    private Long getCurrentUserId() {
        return JwtUser.getAuthenticatedUser().getId();
    }
}
