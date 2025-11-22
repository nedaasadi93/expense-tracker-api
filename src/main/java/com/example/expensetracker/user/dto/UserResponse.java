package com.example.expensetracker.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String mobile;
    private boolean verified;
    private LocalDateTime created;
}
