package com.example.expensetracker.user.controller;

import com.example.expensetracker.user.dto.UserResponse;
import com.example.expensetracker.user.service.UserService;
import com.example.expensetracker.user.statics.UserRestApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User rest controllers")
@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get by ID", description = "Returns user information by ID if it belongs to authenticated user")
    @GetMapping(UserRestApi.USERS_ID)
    public ResponseEntity<UserResponse> getById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
