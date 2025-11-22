package com.example.expensetracker.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserContextDto {
    private Long id;

    public UserContextDto(Long id) {
        this.id = id;
    }
}
