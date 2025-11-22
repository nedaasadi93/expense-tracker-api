package com.example.expensetracker.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    // User
    USER_NOT_FOUND(1, "USER_NOT_FOUND"),
    USER_IS_NOT_VERIFIED(2, "USER_IS_NOT_VERIFIED"),
    USER_IS_NOT_AUTHENTICATED(3, "USER_IS_NOT_AUTHENTICATED"),


    // Security-Auth
    INVALID_TOKEN(10, "INVALID_TOKEN"),
    TOKEN_EXPIRED(11, "TOKEN_EXPIRED"),
    INVALID_TOKEN_OR_EXPIRED(12, "INVALID_TOKEN_OR_EXPIRED"),
    DUPLICATE_MOBILE_NUMBER(13, "DUPLICATE_MOBILE_NUMBER"),
    INVALID_PASSWORD(14, "INVALID_PASSWORD"),
    TOO_MANY_OTP_REQUEST(15, "TO_MANY_OTP_REQUEST"),
    INVALID_OTP(16, "INVALID_OTP");


    private final Integer code;
    private final String message;


    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
