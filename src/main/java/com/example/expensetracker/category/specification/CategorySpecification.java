package com.example.expensetracker.category.specification;

import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class CategorySpecification {

    private static final String WILDCARD = "%";


    public static Specification<CategoryEntity> filter(CategoryFilter filter) {

        return (root, query, cb) -> {
            List<Predicate> predicates = Stream.of(
                            buildEqualPredicate(cb, root.get("userId"), filter.getUserId()),
                            buildLikePredicate(cb, root.get("name"), filter.getName()),
                            buildLikePredicate(cb, root.get("description"), filter.getDescription())
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
        return StringUtils.isNotBlank(value)
                ? cb.like(cb.lower(path), WILDCARD + value.toLowerCase() + WILDCARD) : null;
    }

}
