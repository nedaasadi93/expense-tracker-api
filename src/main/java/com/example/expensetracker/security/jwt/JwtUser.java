package com.example.expensetracker.security.jwt;

import com.example.expensetracker.auth.dto.UserContextDto;
import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtUser {
    public static UserContextDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException(
                    ExceptionModel
                            .builder()
                            .messageKey(ErrorCodes.USER_IS_NOT_AUTHENTICATED.getMessage())
                            .errorCode(ErrorCodes.USER_IS_NOT_AUTHENTICATED.getCode())
                            .build());
        }
        if (authentication.getPrincipal() instanceof UserContextDto userContextDto) {
            return userContextDto;
        }
        throw new NotFoundException(
                ExceptionModel
                        .builder()
                        .messageKey(ErrorCodes.USER_NOT_FOUND.getMessage())
                        .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                        .build());
    }
}
