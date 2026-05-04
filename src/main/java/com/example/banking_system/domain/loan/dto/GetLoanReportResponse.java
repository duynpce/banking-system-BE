package com.example.banking_system.domain.loan.dto;

import com.example.banking_system.domain.loan.constant.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLoanReportResponse {
    private LoanStatus loanStatus;
    private BigDecimal totalAmount;
    private BigDecimal leftAmount;
    private BigDecimal monthlyInstallment;

    public GetLoanReportResponse(BigDecimal totalAmount, BigDecimal leftAmount, BigDecimal monthlyInstallment) {
        this.totalAmount = totalAmount;
        this.leftAmount = leftAmount;
        this.monthlyInstallment = monthlyInstallment;
    }
}