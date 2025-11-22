package com.example.expensetracker.category.dto;

import com.example.expensetracker.common.dto.PageableFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryFilter extends PageableFilter {
    private String name;
    private String description;
    @Schema(hidden = true)
    @Setter(AccessLevel.PRIVATE)
    private Long userId;

    public void putUserId(Long userId) {
        this.userId = userId;
    }
}
