package com.example.expensetracker.report.service;

import com.example.expensetracker.common.util.DateUtil;
import com.example.expensetracker.expense.service.ExpenseService;
import com.example.expensetracker.report.dto.MonthlyReportResponse;
import com.example.expensetracker.report.dto.ReportFilter;
import com.example.expensetracker.security.jwt.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService{
    private final ExpenseService expenseService;

    @Override
    public List<MonthlyReportResponse> generateMonthlyReport(ReportFilter filter) {
        Long userId = getCurrentUserId();

        LocalDateTime start = DateUtil.startOfMonth(filter.getYear(), filter.getMonth());
        LocalDateTime end = DateUtil.endOfMonth(filter.getYear(), filter.getMonth());

        return expenseService.generateMonthlyReportWithAlerts(userId, start, end);
}

    private Long getCurrentUserId() {
        return JwtUser.getAuthenticatedUser().getId();
    }
}

