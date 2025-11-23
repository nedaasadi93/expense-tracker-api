package com.example.expensetracker.expense.service;

import com.example.expensetracker.expense.dto.ExpenseResponse;

public interface ExpenseService {

    ExpenseResponse getById(Long id, Long categoryId);
}
