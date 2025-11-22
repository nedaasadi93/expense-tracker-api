package com.example.expensetracker.user.mapper;

import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(UserEntity user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .mobile(user.getMobile())
                .verified(user.isVerified())
                .created(user.getCreated())
                .build();
    }
}
