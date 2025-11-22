package com.example.expensetracker.user.service;

import com.example.expensetracker.common.exception.ErrorCodes;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.user.domain.UserEntity;
import com.example.expensetracker.user.dto.UserResponse;
import com.example.expensetracker.user.mapper.UserMapper;
import com.example.expensetracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByMobile(String mobile) {
        return userRepository.existsByMobile(mobile);
    }

    @Override
    @Transactional
    public void save(UserEntity entity) {
        userRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    public UserResponse toResponse(UserEntity user) {
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionModel
                                .builder()
                                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                                .messageKey(ErrorCodes.USER_NOT_FOUND.getMessage())
                                .build()));
        return userMapper.toResponse(user);
    }

}
