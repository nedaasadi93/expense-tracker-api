package com.example.expensetracker.common.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableFilter {
    private int page = 0;
    @Min(value = 1, message = "MIN_PAGE_SIZE")
    @Max(value = 500,  message = "MAX_PAGE_SIZE")
    private int pageSize = 20;

    public Pageable toPageable() {
        return PageRequest.of(page, pageSize);
    }
}
