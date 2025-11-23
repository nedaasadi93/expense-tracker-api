package com.example.expensetracker.expense.mapper;

import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseResponse;

import java.util.List;

public interface ExpenseMapper {

    ExpenseResponse toResponse(ExpenseEntity expense);

    List<ExpenseResponse> toResponseList(List<ExpenseEntity> expenses);
}
