package com.example.expensetracker.common.exception;

public class TooManyRequestException extends BaseException {

    public TooManyRequestException(ExceptionModel model) {
        super(model);
    }
}
