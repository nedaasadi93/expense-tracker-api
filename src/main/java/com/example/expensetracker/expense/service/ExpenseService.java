package com.example.expensetracker.expense.service;

import com.example.expensetracker.expense.dto.ExpenseFilter;
import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.dto.ExpenseUpdateRequest;
import org.springframework.data.domain.Page;

public interface ExpenseService {

    ExpenseResponse getById(Long id, Long categoryId);

    Page<ExpenseResponse> getAll(ExpenseFilter filter);

    ExpenseResponse create(ExpenseRequest request, Long categoryId);

    ExpenseResponse update(Long id, ExpenseUpdateRequest request, Long categoryId);

    boolean delete(Long id, Long categoryId);
}
