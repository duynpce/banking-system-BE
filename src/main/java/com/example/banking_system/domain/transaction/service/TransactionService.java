package com.example.banking_system.domain.transaction.service;

import com.example.banking_system.common.dto.MetaDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.TransactionMapper;
import com.example.banking_system.domain.transaction.TransactionRepository;
import com.example.banking_system.domain.transaction.TransactionValidator;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import com.example.banking_system.domain.transaction.dto.TransactionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionQueryService transactionQueryService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;
    private final AccountQueryService accountQueryService;
    private final JwtUtil jwtUtil;


    @Transactional
    public Transaction create(CreateTransactionRequest request) {

        Transaction transaction = transactionMapper.toEntity(request);
        transactionValidator.validateCreate(request);

        switch(transaction.getType()) {
            case TRANSFER -> handleCreateTransferTransaction(transaction);
            case DEPOSIT, CASHBACK -> handleCreateDepositAndCashBackTransaction(transaction);
            case WITHDRAWAL -> handleCreateWithdrawalTransaction(transaction);
            case PAYMENT -> handleCreatePaymentTransaction(transaction);
        }

        return transactionRepository.save(transaction);
    }

    //admin only
    private void handleCreateDepositAndCashBackTransaction(Transaction transaction) {
        String accountNumber = jwtUtil.getJwtClaims().getClaimAsString("account_number");

        Account account = accountQueryService.findByAccountNumber(transaction.getReceiver().getNumber());
        Account internalDepositAccount = accountQueryService.getInternalDePositAccount();

        // if logged in account not internal deposit account --> failed , can convert to role == admin or internal
        if(!accountNumber.equals(accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER())) {
            throw new ValidationException("Only admin can perform this transaction");
        }

        internalDepositAccount.setBalance(internalDepositAccount.getBalance().subtract(transaction.getTransferredAmount()));
        account.setBalance(account.getBalance().add(transaction.getTransferredAmount()));
        accountQueryService.save(account);
        accountQueryService.save(internalDepositAccount);

        transaction.setSender(null);
        transaction.setReceiver(account);
        transaction.setReceiverPostedBalance(account.getBalance());
    }


    private void handleCreateWithdrawalTransaction(Transaction transaction) {
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Account account = accountQueryService.findById(accountId);
        Account internalWithdrawalAccount = accountQueryService.getInternalWithdrawalAccount();

        BigDecimal remainingBalance = account.getBalance().subtract(transaction.getTransferredAmount());
        if(remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Insufficient balance for withdrawal");
        }

        internalWithdrawalAccount.setBalance(internalWithdrawalAccount.getBalance().add(transaction.getTransferredAmount()));
        account.setBalance(remainingBalance);
        accountQueryService.save(account);
        accountQueryService.save(internalWithdrawalAccount);

        transaction.setSender(account);
        transaction.setReceiver(null);
        transaction.setSenderPostedBalance(account.getBalance());
    }

    private void handleCreateTransferTransaction(Transaction transaction) {
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Account sender = accountQueryService.findById(accountId);
        Account receiver = accountQueryService.findByAccountNumber(transaction.getReceiver().getNumber());

        BigDecimal senderRemainingBalance = sender.getBalance().subtract(transaction.getTransferredAmount());
        if(senderRemainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Insufficient balance for transfer");
        }

        if(sender.getId() == receiver.getId()) {
            throw new ValidationException("You cannot transfer to yourself");
        }

        sender.setBalance(senderRemainingBalance);
        receiver.setBalance(receiver.getBalance().add(transaction.getTransferredAmount()));
        accountQueryService.save(sender);
        accountQueryService.save(receiver);

        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setSenderPostedBalance(sender.getBalance());
        transaction.setReceiverPostedBalance(receiver.getBalance());
    }

    private void handleCreatePaymentTransaction(Transaction transaction) {
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Account sender = accountQueryService.findById(accountId);
        Account receiver = accountQueryService.findByAccountNumber(transaction.getReceiver().getNumber());

        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setStatus(TransactionStatus.PENDING);
    }

    //temp ,implement later
    private void acceptPaymentTransaction(Transaction transaction) {

    }


    @Transactional(readOnly = true)
    public ResponseDto<List<GetTransactionResponse>> getByFilter(TransactionFilter transactionFilter) {
        String username = jwtUtil.getUsername();
        Page<Transaction> transactionPage = transactionQueryService.findByFilter(username, transactionFilter);
        MetaDto metaDto = MetaDto.builder().
                totalItems(transactionPage.getTotalElements()).
                totalPages(transactionPage.getTotalPages()).paginationDto(transactionFilter.getPaginationDto()).build();


        return ResponseDto.success(transactionMapper.toDtoList(transactionPage.getContent(),username), "get transaction successfully", metaDto);
    }
}
