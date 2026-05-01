package com.example.banking_system.domain.loan.service.domain;

import com.example.banking_system.common.dto.PaginationDto;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.CreateLoanRequest;
import com.example.banking_system.domain.loan.dto.GetLoanReportResponse;
import com.example.banking_system.domain.loan.dto.GetLoanResponse;
import com.example.banking_system.domain.loan.dto.LoanFilter;
import com.example.banking_system.domain.loan.dto.RepayLoanRequest;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import com.example.banking_system.domain.loan.mapper.LoanMapper;
import com.example.banking_system.domain.loan.service.query.LoanPolicyQueryService;
import com.example.banking_system.domain.loan.service.query.LoanQueryService;
import com.example.banking_system.domain.loan.validator.LoanValidator;
import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import com.example.banking_system.domain.transaction.service.TransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanQueryService loanQueryService;
    private final LoanPolicyQueryService loanPolicyQueryService;
    private final AccountQueryService accountQueryService;
    private final TransactionQueryService transactionQueryService;
    private final LoanMapper loanMapper;
    private final LoanValidator loanValidator;
    private final JwtUtil jwtUtil;

    @Transactional
    public Loan create(CreateLoanRequest request) {
        Loan loan = loanMapper.toEntity(request);
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Account account = accountQueryService.findById(accountId);
        LoanPolicy loanPolicy = loanPolicyQueryService.findById(request.getPolicyId());

        loanValidator.validateCreate(loan, loanPolicy, account);

        loan.setDueDate(LocalDate.now().plusMonths(loanPolicy.getDurationMonths()));
        loan.setAccount(account);
        loan.setPolicy(loanPolicy);
        account.setBalance(account.getBalance().add(request.getAmount()));

        //create a transaction to record the loan lending
        Transaction transaction  = new Transaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReceiver(account);
        transaction.setTransferredAmount(request.getAmount());
        transaction.setDescription("Loan lending for loan id: " + loan.getId());
        transaction.setReceiverPostedBalance(account.getBalance());
        transaction.setType(TransactionType.DEPOSIT);

        return loanQueryService.save(loan);
    }

    @Transactional(readOnly = true)
    public GetLoanResponse getById(long id) {
        Loan loan = loanQueryService.findById(id);
        return loanMapper.toDto(loan);
    }

    @Transactional(readOnly = true)
    public List<GetLoanResponse> getByPage(PaginationDto paginationDto) {
        long AccountId = jwtUtil.getJwtClaims().getClaim("account_id");

        Page<Loan> loanPage = loanQueryService.findByAccountIdWithPagination(AccountId, paginationDto);
        return loanMapper.toDtoList(loanPage.getContent());
    }

    @Transactional(readOnly = true)
    public List<GetLoanResponse> getByFilter(LoanFilter loanFilter) {
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Page<Loan> loanPage = loanQueryService.findByFilter(accountId, loanFilter);
        return loanMapper.toDtoList(loanPage.getContent());
    }

    @Transactional(readOnly = true)
    public GetLoanReportResponse getByReports(LoanStatus loanStatus) {
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        return loanQueryService.findReportByAccountIdAndStatus(accountId, loanStatus);
    }

    @Transactional
    public void repayLoan(RepayLoanRequest request) {
        long accountId = jwtUtil.getJwtClaims().getClaim("account_id");
        Loan loan = loanQueryService.findByIdAndAccountId(request.getLoanId(), accountId);
        Account account = loan.getAccount();

        loanValidator.validateRepay(loan, request, account);

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        loan.setLeftAmount(loan.getLeftAmount().subtract(request.getAmount()));

        //create a transaction to record the loan repayment
        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setSender(account);
        transaction.setTransferredAmount(request.getAmount());
        transaction.setSenderPostedBalance(account.getBalance());
        transaction.setType(TransactionType.PAYMENT);
        transaction.setDescription("Loan repayment for loan id: " + loan.getId());

        if(loan.getLeftAmount().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.DONE_PAYMENT);
        }

        accountQueryService.save(account);
        loanQueryService.save(loan);
        transactionQueryService.save(transaction);
    }
}
