package com.example.expensetracker.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    // User
    USER_NOT_FOUND(1, "USER_NOT_FOUND"),
    USER_IS_NOT_VERIFIED(2, "USER_IS_NOT_VERIFIED"),


    // Security-Auth
    DUPLICATE_MOBILE_NUMBER(10, "DUPLICATE_MOBILE_NUMBER"),
    INVALID_PASSWORD(11, "INVALID_PASSWORD");


    private final Integer code;
    private final String message;


    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
