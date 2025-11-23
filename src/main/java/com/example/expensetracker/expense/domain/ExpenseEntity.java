package com.example.expensetracker.expense.domain;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.common.BaseEntity;
import com.example.expensetracker.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "expense", schema = "expense_tracker")
public class ExpenseEntity  extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_sequence")
    @SequenceGenerator(name = "expense_sequence", sequenceName = "expense_sequence")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "expense_date",nullable = false)
    private LocalDateTime expenseDate;

    @Column(name = "user_id_fk",nullable = false)
    private Long userId;

    @Column(name = "category_id_fk",nullable = false)
    private Long categoryId;

    @JoinColumn(name = "user_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "category_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;
}
