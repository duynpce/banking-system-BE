package com.example.banking_system.domain.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface GetTransactionReportProjection {
    LocalDate getStartDate();

    LocalDate getEndDate();

    BigDecimal getIncomeAmount();

    BigDecimal getOutcomeAmount();

    BigDecimal getIncomeTransferAmount();

    BigDecimal getOutcomeTransferAmount();

    BigDecimal getCashbackAmount();

    BigDecimal getPaymentAmount();

    BigDecimal getDepositAmount();

    BigDecimal getWithdrawalAmount();
}

