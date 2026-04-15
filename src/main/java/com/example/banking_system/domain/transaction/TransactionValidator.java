package com.example.banking_system.domain.transaction;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.service.TransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final TransactionQueryService transactionQueryService;
    private final AccountQueryService accountQueryService;

    public void validateCreate(CreateTransactionRequest request)  {

        if(request.getReceiverAccountNumber().equals(accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER()) ||
        request.getReceiverAccountNumber().equals(accountQueryService.getINTERNAL_WITHDRAWAL_ACCOUNT_NUMBER())) {
            throw new ValidationException("Invalid receiver account number");
        }

    }

}
