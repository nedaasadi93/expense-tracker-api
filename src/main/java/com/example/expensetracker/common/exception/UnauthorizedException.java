package com.example.expensetracker.common.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ExceptionModel model) {
        super(model);
    }
}
