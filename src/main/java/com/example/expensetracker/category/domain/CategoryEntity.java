package com.example.expensetracker.category.domain;

import com.example.expensetracker.common.BaseEntity;
import com.example.expensetracker.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "category", schema = "expense_tracker")
public class CategoryEntity  extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_sequence")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "user_id_fk",nullable = false)
    private Long userId;

    @JoinColumn(name = "user_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "monthly_limit", nullable = false)
    private BigDecimal monthlyLimit;


}
