package com.example.banking_system.domain.loan.dto;

import java.math.BigDecimal;

public interface GetLoanReportProjection {
    public BigDecimal getTotalAmount();
    public BigDecimal getLeftAmount();
    public BigDecimal getMonthlyInstallment();
}
