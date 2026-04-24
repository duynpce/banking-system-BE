package com.example.banking_system.domain.transaction.dto;

import com.example.banking_system.domain.transaction.constant.TransactionReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public  class GetTransactionReport {
    private TransactionReportType reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal incomeAmount;
    private BigDecimal outcomeAmount;
    private BigDecimal incomeTransferAmount;
    private BigDecimal outcomeTransferAmount;
    private BigDecimal cashbackAmount;
    private BigDecimal paymentAmount;
    private BigDecimal depositAmount;
    private BigDecimal withdrawalAmount;

    public GetTransactionReport(LocalDate startDate, LocalDate endDate, BigDecimal incomeAmount, BigDecimal outcomeAmount, BigDecimal incomeTransferAmount, BigDecimal outcomeTransferAmount, BigDecimal cashbackAmount, BigDecimal paymentAmount, BigDecimal depositAmount, BigDecimal withdrawalAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.incomeAmount = incomeAmount;
        this.outcomeAmount = outcomeAmount;
        this.incomeTransferAmount = incomeTransferAmount;
        this.outcomeTransferAmount = outcomeTransferAmount;
        this.cashbackAmount = cashbackAmount;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
        this.withdrawalAmount = withdrawalAmount;
    }
}
