package com.example.expensetracker.expense.statics;

public abstract class ExpenseRestApi {

    public static final String EXPENSES = "categories/{categoryId}/expenses";
    public static final String EXPENSES_ID = "categories/{categoryId}/expenses/{id}";
    public static final String EXPENSES_MONTHLY = "/categories/{categoryId}/expenses/monthly";
    public static final String CHECK_LIMIT_EXCEED = "/categories/{categoryId}/expenses/check-limit-exceed";
}
