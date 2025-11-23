package com.example.expensetracker.expense.controller;

import com.example.expensetracker.expense.dto.ExpenseFilter;
import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.dto.ExpenseUpdateRequest;
import com.example.expensetracker.expense.service.ExpenseService;
import com.example.expensetracker.expense.statics.ExpenseRestApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Expense", description = "Expense rest controllers")
@RestController
@RequestMapping("${rest.idn}")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Get by ID", description = "Returns an expense by its ID if it belongs to the specified category and is owned by the authenticated user")
    @GetMapping(path = ExpenseRestApi.EXPENSES_ID)
    public ResponseEntity<ExpenseResponse> getById(@PathVariable(value = "categoryId") Long categoryId, @PathVariable(value = "id") Long id){
        return ResponseEntity.ok(expenseService.getById(id,categoryId));
    }

    @Operation(summary = "Get all", description = "Returns all expenses if they belongs to the specified category and is owned by the authenticated user")
    @GetMapping(path = ExpenseRestApi.EXPENSES)
    public  ResponseEntity<Page<ExpenseResponse>> getAll(@PathVariable(value = "categoryId") Long categoryId, @Valid ExpenseFilter filter){
        filter.putCategoryId(categoryId);
        return ResponseEntity.ok(expenseService.getAll(filter));
    }

    @Operation(summary = "Create", description = "creates an expenses in the specified category for the authenticated user")
    @PostMapping(path = ExpenseRestApi.EXPENSES)
    public ResponseEntity<ExpenseResponse> create(@PathVariable(value = "categoryId") Long categoryId, @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.create(request,categoryId));
    }

    @Operation(summary = "Update", description = "Updates an expense in the specified category if it is owned by the authenticated user")
    @PutMapping(path = ExpenseRestApi.EXPENSES_ID)
    public ResponseEntity<ExpenseResponse> update(@PathVariable(value = "categoryId") Long categoryId, @PathVariable(value = "id") Long id, @Valid @RequestBody ExpenseUpdateRequest request){
        return ResponseEntity.ok(expenseService.update(id,request,categoryId));
    }

    @Operation(summary = "Delete", description = "Deletes an expense in the specified category if it is owned by the authenticated user")
    @DeleteMapping(path = ExpenseRestApi.EXPENSES_ID)
    public ResponseEntity<Boolean> delete(@PathVariable(value = "categoryId") Long categoryId, @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(expenseService.delete(id,categoryId));
    }
}
