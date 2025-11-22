package com.example.expensetracker.common.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final ExceptionModel model;

    public ConflictException(ExceptionModel model) {
        super(model.getMessageKey());
        this.model = model;
    }
}
