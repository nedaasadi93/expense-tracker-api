package com.example.expensetracker.user.controller;

import com.example.expensetracker.user.dto.UserResponse;
import com.example.expensetracker.user.service.UserService;
import com.example.expensetracker.user.statics.UserRestApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(UserRestApi.USERS_ID)
    public ResponseEntity<UserResponse> getById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
