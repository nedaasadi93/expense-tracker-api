package com.example.expensetracker.expense.specification;

import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class ExpenseSpecification {

    private static final String WILDCARD = "%";

    public static Specification<ExpenseEntity> filter(ExpenseFilter filter) {
        return (root, query, cb) -> {
            root.fetch("category", JoinType.INNER);
            query.distinct(true);
            List<Predicate> predicates = Stream.of(
                            buildEqualPredicate(cb, root.get("userId"), filter.getUserId()),
                            buildEqualPredicate(cb, root.get("category").get("id"), filter.getCategoryId()),
                            buildLikePredicate(cb, root.get("name"), filter.getName()),
                            buildLikePredicate(cb, root.get("description"), filter.getDescription()),
                            buildEqualPredicate(cb, root.get("amount"), filter.getAmount()),
                            buildEqualPredicate(cb, root.get("expenseDate"), filter.getExpenseDate()),
                            buildBetweenPredicate(cb, root.get("expenseDate"), filter.getStartDate(), filter.getEndDate())
                    )
                    .filter(Objects::nonNull)
                    .toList();

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildEqualPredicate(CriteriaBuilder cb, Path<?> path, Object value) {
        return value != null ? cb.equal(path, value) : null;
    }

    private static Predicate buildLikePredicate(CriteriaBuilder cb, Path<String> path, String value) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(value)
                ? cb.like(cb.lower(path), WILDCARD + value.toLowerCase() + WILDCARD) : null;
    }

    private static <T extends Comparable<? super T>> Predicate buildBetweenPredicate(CriteriaBuilder cb, Path<T> path, T startValue, T endValue) {
        return (startValue != null && endValue != null) ? cb.between(path, startValue, endValue) : null;
    }
}
