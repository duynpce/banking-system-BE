package com.example.banking_system.domain.transaction.dto;

import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import lombok.Data;

import java.time.Instant;

@Data
public class GetTransactionResponse {
    private long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private TransactionType type;
    private TransactionStatus status;
    private String transferredAmount;
    private String postedBalance;
    private Instant createdAt;
}
