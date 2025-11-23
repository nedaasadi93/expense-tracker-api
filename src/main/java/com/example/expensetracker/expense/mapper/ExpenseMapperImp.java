package com.example.expensetracker.expense.mapper;

import com.example.expensetracker.category.mapper.CategoryMapper;
import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.dto.ExpenseUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseMapperImp implements ExpenseMapper{

    private final CategoryMapper categoryMapper;

    @Override
    public ExpenseResponse toResponse(ExpenseEntity expense) {
        if (expense == null) {
            return null;
        }
        return ExpenseResponse.builder()
                .id(expense.getId())
                .userId(expense.getUserId())
                .name(expense.getName())
                .description(expense.getDescription())
                .categoryId(expense.getCategoryId())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .category(
                        (Hibernate.isInitialized(expense.getCategory()) && expense.getCategory()!=null)
                                ? categoryMapper.toResponse(expense.getCategory())
                                : null
                )
                .build();
    }

    @Override
    public List<ExpenseResponse> toResponseList(List<ExpenseEntity> expenses) {
        return expenses.stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
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

    @Override
    public void updateEntity(ExpenseUpdateRequest request, ExpenseEntity entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setAmount(request.getAmount());
        entity.setExpenseDate(request.getExpenseDate());
    }


}
