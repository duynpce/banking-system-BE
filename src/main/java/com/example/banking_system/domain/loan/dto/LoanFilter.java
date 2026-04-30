package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.constant.LoanType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanFilter {

    @NotNull(message = "paginationDto is required")
    @Valid
    private PaginationDto paginationDto;

    private LoanStatus status;

    private LoanType loanType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @AssertTrue(message = "endDate is required when startDate is provided")
    public boolean isEndDateRequiredWhenStartDateExists() {
        return (startDate == null && endDate == null) || (startDate != null && endDate != null);
    }

    @AssertTrue(message = "startDate must be before or equal to endDate")
    public boolean isDateRangeValid() {
        return startDate == null || !startDate.isAfter(endDate);
    }
}

