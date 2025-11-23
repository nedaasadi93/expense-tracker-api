package com.example.expensetracker.user.mapper;

import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.dto.UserResponse;


public interface UserMapper {
     UserResponse toResponse(UserEntity user);
}
