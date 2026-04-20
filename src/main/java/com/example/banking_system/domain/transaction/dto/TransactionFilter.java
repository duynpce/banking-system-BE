package com.example.banking_system.domain.transaction.dto;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.domain.transaction.constant.TransactionGroup;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionFilter {

    @NotNull(message = "pagination dto is required")
    @Valid
    private PaginationDto paginationDto;

    @NotNull(message = "transaction group is required")
    private TransactionGroup transactionGroup;

    private TransactionType type;
    private TransactionStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @AssertTrue(message = "endDate is required when startDate is provided")
    public boolean isEndDateRequiredWhenStartDateExists() {
        return (startDate == null &&  endDate == null) || (startDate != null && endDate != null);
    }

    @AssertTrue(message = "startDate must be before or equal to endDate")
    public boolean isDateRangeValid() {
        return startDate == null || !startDate.isAfter(endDate);
    }
}
