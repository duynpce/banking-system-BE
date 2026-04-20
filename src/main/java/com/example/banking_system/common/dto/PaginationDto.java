package com.example.banking_system.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginationDto {

    @NotNull(message = "page is required")
    @Min(value = 0, message = "page cannot be negative")
    private Integer page;

    @NotNull(message = "limit is required")
    @Min(value = 1, message = "limit must be greater than 0")
    private Integer limit;
}
