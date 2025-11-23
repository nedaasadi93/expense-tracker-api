package com.example.expensetracker.common.exception;

public class BadRequestException extends BaseException {
    public BadRequestException(ExceptionModel model) {
        super(model);
    }
}
