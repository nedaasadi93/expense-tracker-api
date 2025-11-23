package com.example.expensetracker.expense.repository;

import com.example.expensetracker.expense.domain.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

    @Query("""
                SELECT e, c
                FROM ExpenseEntity e
                JOIN FETCH e.category c
                WHERE e.userId = :userId AND e.id = :id
            """)
    Optional<ExpenseEntity> findByIdAndUserIdWithCategory(Long id, Long userId);


    @Query("""
                SELECT SUM(e.amount)
                FROM ExpenseEntity e
                WHERE e.userId = :userId AND e.category.id = :categoryId
                      AND e.expenseDate BETWEEN :start AND :end
            """)
    BigDecimal totalSpentInCategory(Long userId, Long categoryId, LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT e, c
                FROM ExpenseEntity e
                JOIN FETCH e.category c
                WHERE e.userId = :userId AND e.category.id = :categoryId
                      AND e.expenseDate BETWEEN :start AND :end
            """)
    List<ExpenseEntity> findByUserIdAndCategoryIdAndExpenseDateBetween(Long userId, Long categoryId, LocalDateTime start, LocalDateTime end);
}
