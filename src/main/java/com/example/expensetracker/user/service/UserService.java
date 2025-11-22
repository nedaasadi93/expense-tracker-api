package com.example.expensetracker.user.service;

import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.dto.UserResponse;

import java.util.Optional;

public interface UserService {

    boolean existsByMobile(String mobile);

    void save(UserEntity entity);

    Optional<UserEntity> findByMobile(String mobile);

    UserResponse toResponse(UserEntity user);

    Optional<UserEntity> findById(Long userId);

    UserResponse getById(Long id);
}
