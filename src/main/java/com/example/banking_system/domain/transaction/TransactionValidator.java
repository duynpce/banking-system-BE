package com.example.banking_system.domain.transaction;

import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final AccountQueryService accountQueryService;

    public void validateCreate(CreateTransactionRequest request, Account  loggedInAccount){

        boolean receiverAccountNumberIsInternalAccount = request.getReceiverAccountNumber() != null && (request.getReceiverAccountNumber().equals(accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER()) ||
                request.getReceiverAccountNumber().equals(accountQueryService.getINTERNAL_WITHDRAWAL_ACCOUNT_NUMBER()));

        if(receiverAccountNumberIsInternalAccount) {
            throw new ValidationException("Invalid receiver account number");
        }

        if(!loggedInAccount.getStatus().canPerformTransactions()){
            throw new ForbiddenException("account with status " +  loggedInAccount.getStatus() + " cannot perform transactions");
        }

        if(!loggedInAccount.getCreditRank().canOpenDeposit() && request.getType() == TransactionType.DEPOSIT){
            throw new ForbiddenException("your credit is not enough to take out deposit");
        }

    }

}
