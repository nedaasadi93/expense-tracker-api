package com.example.expensetracker.expense.repository;

import com.example.expensetracker.expense.domain.ExpenseEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExpenseRepository {

    @Query("""
                SELECT e, c
                FROM ExpenseEntity e
                JOIN FETCH e.category c
                WHERE e.userId = :userId AND e.id = :id
            """)
    Optional<ExpenseEntity> findByIdAndUserIdWithCategory(Long id, Long userId);
}
