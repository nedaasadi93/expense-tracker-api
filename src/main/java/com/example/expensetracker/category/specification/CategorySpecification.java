package com.example.expensetracker.category.specification;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecification {
    public static Specification<CategoryEntity> filter(CategoryFilter filter) {
        return null;
    }
}
