package com.example.expensetracker.expense.service;

import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.dto.ExpenseUpdateRequest;

public interface ExpenseService {

    ExpenseResponse getById(Long id, Long categoryId);

    ExpenseResponse create(ExpenseRequest request, Long categoryId);

    ExpenseResponse update(Long id, ExpenseUpdateRequest request, Long categoryId);
}
