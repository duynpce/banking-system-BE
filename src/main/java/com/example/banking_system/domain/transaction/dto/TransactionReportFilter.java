package com.example.banking_system.domain.transaction.dto;

import com.example.banking_system.domain.transaction.constant.TransactionReportType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportFilter {
    @NotNull(message ="report type is required")
    private TransactionReportType reportType;

    @Min(value = 1, message = "day must be at least 1")
    @Max(value = 31, message = "day cannot be greater than 31")
    private Integer day;

    // weeks in a month
    @Min(value = 1, message = "week must be at least 1")
    @Max(value = 5, message = "week cannot be greater than 5")
    private Integer week;

    @Min(value = 1, message = "month must be at least 1")
    @Max(value = 12, message = "month cannot be greater than 12")
    private Integer month;


    @Min(value = 2000, message = "year must be at least 2000")
    private Integer year;
}
