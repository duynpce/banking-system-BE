package com.example.banking_system.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaDto {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
