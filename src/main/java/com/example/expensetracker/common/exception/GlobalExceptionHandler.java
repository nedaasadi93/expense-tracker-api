package com.example.expensetracker.common.exception;

import com.example.expensetracker.configs.MessageBundleConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageBundleConfig messageBundleConfig;

    private ErrorResponse buildError(HttpStatus status, String message, Object[] args, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(errorMessageBuilder(message, args))
                .path(request.getRequestURI())
                .build();
    }

    private String errorMessageBuilder(String messageKey, Object[] args) {
        String message;
        try {
            MessageSource messageSource = messageBundleConfig.messageSource();
            message = messageSource.getMessage(messageKey, args, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            log.error(messageKey, e.getMessage());
            message = messageKey;
        }

        return message;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getModel().getMessageKey(), ex.getModel().getArgs(), request));
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, ex.getModel().getMessageKey(), ex.getModel().getArgs(), request));
    }


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getModel().getMessageKey(), ex.getModel().getArgs(), request));
    }


    @ExceptionHandler(UserMobileUnverifiedException.class)
    public ResponseEntity<ErrorResponse> handleUnverifiedMobile(UserMobileUnverifiedException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.LOCKED)
                .body(buildError(HttpStatus.LOCKED, ex.getModel().getMessageKey(), ex.getModel().getArgs(), request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null, request));
    }
}
