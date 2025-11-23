package com.example.expensetracker.common.exception;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionModel {

    private String messageKey;
    private Object[] args;
    private int errorCode;
}