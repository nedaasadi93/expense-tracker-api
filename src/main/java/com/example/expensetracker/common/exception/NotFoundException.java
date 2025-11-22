package com.example.expensetracker.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {
    public NotFoundException(ExceptionModel model) {
        super(model);
    }
}
