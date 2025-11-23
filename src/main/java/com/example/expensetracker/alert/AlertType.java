package com.example.expensetracker.alert;

import lombok.Getter;

@Getter
public enum AlertType {
    LIMIT_EXCEEDED,
    WITHIN_LIMIT,
    CUSTOM;
}
