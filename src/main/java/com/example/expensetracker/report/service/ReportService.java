package com.example.expensetracker.report.service;

import com.example.expensetracker.report.dto.MonthlyReportResponse;
import com.example.expensetracker.report.dto.ReportFilter;

import java.util.List;

public interface ReportService {

    List<MonthlyReportResponse> generateMonthlyReport(ReportFilter filter);
}
