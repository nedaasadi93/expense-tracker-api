package com.example.expensetracker.expense.mapper;

import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.dto.ExpenseUpdateRequest;

import java.util.List;

public interface ExpenseMapper {

    ExpenseResponse toResponse(ExpenseEntity expense);

    List<ExpenseResponse> toResponseList(List<ExpenseEntity> expenses);

    ExpenseEntity createExpense(ExpenseRequest request, Long userId, Long categoryId);

    void updateEntity(ExpenseUpdateRequest request, ExpenseEntity entity);
}
