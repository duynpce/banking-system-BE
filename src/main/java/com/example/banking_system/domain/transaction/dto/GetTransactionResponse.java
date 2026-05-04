package com.example.banking_system.domain.transaction.dto;

import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class GetTransactionResponse {
    private long id;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String description;
    private TransactionType type;
    private TransactionStatus status;
    private BigDecimal transferredAmount;
    private BigDecimal PostedBalance;
    private Instant createdAt;
}
