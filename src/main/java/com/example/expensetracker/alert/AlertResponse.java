package com.example.expensetracker.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {
    private AlertType type;
    private String message;
    private BigDecimal currentAmount;
    private BigDecimal monthlyLimit;
}
