package com.example.banking_system.domain.transaction.service;

import com.example.banking_system.common.dto.MetaDto;
import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.common.utility.TimeUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.TransactionMapper;
import com.example.banking_system.domain.transaction.TransactionRepository;
import com.example.banking_system.domain.transaction.TransactionValidator;
import com.example.banking_system.domain.transaction.constant.TransactionReportType;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.dto.CreateTransactionRequest;
import com.example.banking_system.domain.transaction.dto.GetTransactionReport;
import com.example.banking_system.domain.transaction.dto.GetTransactionResponse;
import com.example.banking_system.domain.transaction.dto.TransactionFilter;
import com.example.banking_system.domain.transaction.dto.TransactionReportFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final TimeUtil timeUtil;


    @Transactional
    public Transaction create(CreateTransactionRequest request) {

        Transaction transaction = transactionMapper.toEntity(request);
        Account loggedInAccount = accountQueryService.findById(jwtUtil.getJwtClaims().getClaim("account_id"));
        transactionValidator.validateCreate(request, loggedInAccount);

        switch(transaction.getType()) {
            case TRANSFER -> handleCreateTransferTransaction(transaction,  loggedInAccount);
            case DEPOSIT, CASHBACK -> handleCreateDepositAndCashBackTransaction(transaction,   loggedInAccount);
            case WITHDRAWAL -> handleCreateWithdrawalTransaction(transaction,   loggedInAccount);
            case PAYMENT -> handleCreatePaymentTransaction(transaction,   loggedInAccount);
        }

        return transactionRepository.save(transaction);
    }

    //admin only
    private void handleCreateDepositAndCashBackTransaction(Transaction transaction, Account loggedInAccount) {
        Account internalDepositAccount = accountQueryService.getInternalDePositAccount();
        Account receiver = accountQueryService.findByAccountNumber(transaction.getReceiver().getNumber());

        // if logged in account not internal deposit account --> failed , can convert to role == admin or internal
        if(!loggedInAccount.getNumber().equals(accountQueryService.getINTERNAL_DEPOSIT_ACCOUNT_NUMBER())) {
            throw new ValidationException("Only admin can perform this transaction");
        }

        internalDepositAccount.setBalance(internalDepositAccount.getBalance().subtract(transaction.getTransferredAmount()));
        receiver.setBalance(receiver.getBalance().add(transaction.getTransferredAmount()));
        accountQueryService.save(receiver);
        accountQueryService.save(internalDepositAccount);

        transaction.setSender(null);
        transaction.setReceiver(receiver);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReceiverPostedBalance(receiver.getBalance());
    }


    private void handleCreateWithdrawalTransaction(Transaction transaction,  Account loggedInAccount) {
        Account internalWithdrawalAccount = accountQueryService.getInternalWithdrawalAccount();

        BigDecimal remainingBalance = loggedInAccount.getBalance().subtract(transaction.getTransferredAmount());
        if(remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Insufficient balance for withdrawal");
        }

        if(!loggedInAccount.getStatus().canWithdraw()){
            throw new ForbiddenException("account with status " +  loggedInAccount.getStatus() + " cannot perform withdrawal transactions");
        }

        internalWithdrawalAccount.setBalance(internalWithdrawalAccount.getBalance().add(transaction.getTransferredAmount()));
        loggedInAccount.setBalance(remainingBalance);
        accountQueryService.save(loggedInAccount);
        accountQueryService.save(internalWithdrawalAccount);

        transaction.setSender(loggedInAccount);
        transaction.setReceiver(null);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setSenderPostedBalance(loggedInAccount.getBalance());
    }

    private void handleCreateTransferTransaction(Transaction transaction, Account sender) {
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

    private void handleCreatePaymentTransaction(Transaction transaction , Account sender) {
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

    @Transactional(readOnly = true)
    public ResponseDto<List<GetTransactionReport>> getReports(TransactionReportFilter transactionReportFilter) {
        List<GetTransactionReport> response = switch (transactionReportFilter.getReportType()) {
            case DAY -> getDayReport(transactionReportFilter);
            case WEEK -> getWeekReport(transactionReportFilter);
            case MONTH -> getMonthReport(transactionReportFilter);
            case YEAR -> getYearReport(transactionReportFilter);
        };

        return ResponseDto.success(response, "Transaction report retrieved successfully");
    }

    private List<GetTransactionReport> getDayReport(TransactionReportFilter filter) {

        if(filter.getDay() == null) {
            throw new ValidationException("day is required for daily report");
        }

        if(filter.getMonth() == null) {
            throw new ValidationException("month is required for daily report");
        }

        if(filter.getYear() == null) {
            throw new ValidationException("year is required for daily report");
        }

        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");

        LocalDate startDate = timeUtil.getDayStartDate(filter.getYear(), filter.getMonth(), filter.getDay());
        LocalDate endDate = timeUtil.getDayEndDate(filter.getYear(), filter.getMonth(), filter.getDay());

        return transactionQueryService.getTransactionReportByAccountAndDateRange(
                accountId,
                startDate,
                endDate,
                "day",
                TransactionReportType.DAY
        );
    }

    private List<GetTransactionReport> getWeekReport(TransactionReportFilter filter) {

        if(filter.getWeek() == null) {
            throw new ValidationException("week is required for weekly report");
        }

        if(filter.getMonth() == null) {
            throw new ValidationException("month is required for weekly report");
        }

        if(filter.getYear() == null) {
            throw new ValidationException("year is required for weekly report");
        }

        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");

        LocalDate startDate = timeUtil.getWeekStartDate(filter.getYear(), filter.getMonth(), filter.getWeek());
        LocalDate endDate = timeUtil.getWeekEndDate(filter.getYear(), filter.getMonth(), filter.getWeek());

        return transactionQueryService.getTransactionReportByAccountAndDateRange(
                accountId,
                startDate,
                endDate,
                "day",
                TransactionReportType.DAY
        );
    }

    private List<GetTransactionReport> getMonthReport(TransactionReportFilter filter) {

        if(filter.getMonth() == null) {
            throw new ValidationException("month is required for monthly report");
        }

        if(filter.getYear() == null) {
            throw new ValidationException("year is required for monthly report");
        }

        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");

        LocalDate startDate = timeUtil.getMonthStartDate(filter.getYear(), filter.getMonth());
        LocalDate endDate = timeUtil.getMonthEndDate(filter.getYear(), filter.getMonth());

        return transactionQueryService.getTransactionReportByAccountAndDateRange(
                accountId,
                startDate,
                endDate,
                "week",
                TransactionReportType.WEEK
        );
    }

    private List<GetTransactionReport> getYearReport(TransactionReportFilter filter) {

        if(filter.getYear() == null){
            throw new ValidationException("year is required for year report");
        }

        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");

        LocalDate startDate = timeUtil.getYearStartDate(filter.getYear());
        LocalDate endDate = timeUtil.getYearEndDate(filter.getYear());

        return transactionQueryService.getTransactionReportByAccountAndDateRange(
                accountId,
                startDate,
                endDate,
                "month",
                TransactionReportType.MONTH
        );
    }

}
