package com.example.expensetracker.expense.service;

import com.example.expensetracker.alert.AlertResponse;
import com.example.expensetracker.expense.dto.*;
import com.example.expensetracker.report.dto.MonthlyReportResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse getById(Long id, Long categoryId);

    Page<ExpenseResponse> getAll(ExpenseFilter filter);

    ExpenseResponse create(ExpenseRequest request, Long categoryId);

    ExpenseResponse update(Long id, ExpenseUpdateRequest request, Long categoryId);

    boolean delete(Long id, Long categoryId);

    List<ExpenseResponse> getMonthlyExpenses(Long categoryId, int year, int month);

    AlertResponse checkLimitExceed(Long categoryId, CheckLimitExceedRequest request);

    List<MonthlyReportResponse> generateMonthlyReportWithAlerts(Long userId, LocalDateTime start, LocalDateTime end);
}
