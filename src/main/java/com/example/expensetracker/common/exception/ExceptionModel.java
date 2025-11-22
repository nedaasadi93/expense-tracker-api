package com.example.expensetracker.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExceptionModel {

    private String messageKey;
    private Object[] args;
    private int errorCode;
}