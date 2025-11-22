package com.example.expensetracker.user.service;

import com.example.expensetracker.user.domain.UserEntity;

public interface UserService {

    boolean existsByMobile(String mobile);

    void save(UserEntity entity);
}
