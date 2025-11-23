package com.example.expensetracker.report.controller;

import com.example.expensetracker.report.dto.MonthlyReportResponse;
import com.example.expensetracker.report.dto.ReportFilter;
import com.example.expensetracker.report.service.ReportService;
import com.example.expensetracker.report.statics.ReportRestApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Report", description = "Report rest controllers")
@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Monthly Report", description = "Returns the monthly report, including category totals, limits, and alerts for exceeded limits for the authenticated user")
    @GetMapping(path = ReportRestApi.MONTHLY_REPORT)
    public ResponseEntity<List<MonthlyReportResponse>> generateMonthlyReport(@Valid ReportFilter filter){
        return ResponseEntity.ok(reportService.generateMonthlyReport(filter));
    }
}
