package com.example.expensetracker.common.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ExceptionModel model;

    protected BaseException(ExceptionModel model) {
        super(model.getMessageKey());
        this.model = model;
    }
}
