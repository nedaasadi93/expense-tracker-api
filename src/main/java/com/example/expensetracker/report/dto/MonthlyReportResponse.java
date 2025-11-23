package com.example.expensetracker.report.dto;

import java.math.BigDecimal;

public interface MonthlyReportResponse {
    Long getCategoryId();
    String getCategoryName();
    BigDecimal getTotalAmount();
    BigDecimal getCategoryLimit();
    String getAlertType();
}
