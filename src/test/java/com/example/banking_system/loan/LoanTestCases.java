package com.example.banking_system.loan;

import com.example.banking_system.account.AccountTestCases;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.dto.*;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanFine;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
public class LoanTestCases {
    private static LoanTestCases instance;

    private final AccountTestCases accountTestCases = AccountTestCases.getInstance();

    public static LoanTestCases getInstance() {
        if (instance == null) {
            instance = new LoanTestCases();
        }

        return instance;
    }

    public CreateLoanPolicyRequest getCreateLoanPolicyRequestTestCase() {
        CreateLoanPolicyRequest request = new CreateLoanPolicyRequest();
        request.setDurationMonths(12);
        request.setInterestRate(1.5);
        request.setMaxAmount(10000.00);
        request.setLoanType(LoanType.CREDIT);
        request.setEffectiveFrom(LocalDate.now());
        request.setEffectiveTo(LocalDate.now().plusMonths(12));
        return request;
    }

    public UpdateLoanPolicyRequest getUpdateLoanPolicyRequestTestCase() {
        UpdateLoanPolicyRequest request = new UpdateLoanPolicyRequest();
        request.setId(1L);
        request.setDurationMonths(24);
        request.setInterestRate(2.0);
        request.setLoanType(LoanType.CREDIT);
        request.setEffectiveFrom(LocalDate.now().plusDays(1));
        request.setEffectiveTo(LocalDate.now().plusMonths(18));
        return request;
    }

    public LoanPolicy getLoanPolicyTestCase() {
        CreateLoanPolicyRequest request = getCreateLoanPolicyRequestTestCase();
        LoanPolicy loanPolicy = new LoanPolicy();
        loanPolicy.setId(1L);
        loanPolicy.setDurationMonths(request.getDurationMonths());
        loanPolicy.setInterestRate(request.getInterestRate());
        loanPolicy.setLoanType(request.getLoanType());
        loanPolicy.setEffectiveFrom(request.getEffectiveFrom());
        loanPolicy.setEffectiveTo(request.getEffectiveTo());
        loanPolicy.setMaxAmount(BigDecimal.valueOf(request.getMaxAmount()));
        loanPolicy.setCreatedAt(Instant.now());
        loanPolicy.setMaxAmount(BigDecimal.valueOf(request.getMaxAmount()));
        return loanPolicy;
    }

    public GetLoanPolicyResponse getLoanPolicyResponseTestCase() {
        LoanPolicy loanPolicy = getLoanPolicyTestCase();
        GetLoanPolicyResponse response = new GetLoanPolicyResponse();
        response.setId(loanPolicy.getId());
        response.setDurationMonths(loanPolicy.getDurationMonths());
        response.setInterestRate(loanPolicy.getInterestRate());
        response.setLoanType(loanPolicy.getLoanType());
        response.setEffectiveFrom(loanPolicy.getEffectiveFrom());
        response.setEffectiveTo(loanPolicy.getEffectiveTo());
        response.setCreatedAt(loanPolicy.getCreatedAt());
        response.setMaxAmount(loanPolicy.getMaxAmount());
        return response;
    }

    public CreateLoanFinePolicyRequest getCreateLoanFinePolicyRequestTestCase() {
        CreateLoanFinePolicyRequest request = new CreateLoanFinePolicyRequest();
        request.setLoanFineType(LoanFineType.OVERDUE_PAYMENT);
        request.setAmount(new BigDecimal("25.00"));
        request.setEffectiveFrom(LocalDate.now());
        request.setEffectiveTo(LocalDate.now().plusMonths(6));
        return request;
    }

    public UpdateLoanFinePolicyRequest getUpdateLoanFinePolicyRequestTestCase() {
        UpdateLoanFinePolicyRequest request = new UpdateLoanFinePolicyRequest();
        request.setId(1L);
        request.setLoanFineType(LoanFineType.OVERDUE_PAYMENT);
        request.setAmount(new BigDecimal("50.00"));
        request.setEffectiveFrom(LocalDate.now().plusDays(1));
        request.setEffectiveTo(LocalDate.now().plusMonths(9));
        return request;
    }

    public LoanFinePolicy getLoanFinePolicyTestCase() {
        CreateLoanFinePolicyRequest request = getCreateLoanFinePolicyRequestTestCase();
        LoanFinePolicy loanFinePolicy = new LoanFinePolicy();
        loanFinePolicy.setId(1L);
        loanFinePolicy.setLoanFineType(request.getLoanFineType());
        loanFinePolicy.setAmount(request.getAmount());
        loanFinePolicy.setEffectiveFrom(request.getEffectiveFrom());
        loanFinePolicy.setEffectiveTo(request.getEffectiveTo());
        loanFinePolicy.setCreatedAt(Instant.now());
        return loanFinePolicy;
    }

    public GetLoanFinePolicyResponse getLoanFinePolicyResponseTestCase() {
        LoanFinePolicy loanFinePolicy = getLoanFinePolicyTestCase();
        GetLoanFinePolicyResponse response = new GetLoanFinePolicyResponse();
        response.setId(loanFinePolicy.getId());
        response.setType(loanFinePolicy.getLoanFineType());
        response.setAmount(loanFinePolicy.getAmount());
        response.setEffectiveFrom(loanFinePolicy.getEffectiveFrom());
        response.setEffectiveTo(loanFinePolicy.getEffectiveTo());
        response.setCreatedAt(loanFinePolicy.getCreatedAt());
        return response;
    }

    public CreateLoanRequest getCreateLoanRequestTestCase(long policyId, LoanType type) {
        CreateLoanRequest request = new CreateLoanRequest();
        request.setAmount(new BigDecimal("1000.00"));
        request.setType(type);
        request.setPolicyId(policyId);
        return request;
    }

    public CreateLoanRequest getCreateLoanRequestTestCase() {
        return getCreateLoanRequestTestCase(1L, LoanType.CREDIT);
    }

    public RepayLoanRequest getRepayLoanRequestTestCase(long loanId, BigDecimal amount) {
        RepayLoanRequest request = new RepayLoanRequest();
        request.setLoanId(loanId);
        request.setAmount(amount);
        return request;
    }

    public Loan getLoanTestCase(Account account, LoanPolicy loanPolicy) {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setTotalAmount(new BigDecimal("1000.00"));
        loan.setLeftAmount(new BigDecimal("1000.00"));
        loan.setDueDate(LocalDate.now().plusMonths(loanPolicy.getDurationMonths()));
        loan.setStatus(LoanStatus.CURRENT_PAYMENT);
        loan.setType(loanPolicy.getLoanType());
        loan.setCreatedAt(LocalDate.now());
        loan.setAccount(account);
        loan.setPolicy(loanPolicy);
        return loan;
    }

    public Loan getLoanTestCase() {
        Account account = accountTestCases.getPersonalAccountTestCase().getAccount();
        LoanPolicy loanPolicy = getLoanPolicyTestCase();
        return getLoanTestCase(account, loanPolicy);
    }

    public GetLoanResponse getLoanResponseTestCase() {
        Loan loan = getLoanTestCase();
        GetLoanResponse response = new GetLoanResponse();
        response.setId(loan.getId());
        response.setTotalAmount(loan.getTotalAmount());
        response.setLeftAmount(loan.getLeftAmount());
        response.setDueDate(loan.getDueDate());
        response.setStatus(loan.getStatus());
        response.setType(loan.getType());
        response.setCreatedAt(loan.getCreatedAt());
        return response;
    }

    public CreateLoanFineRequest getCreateLoanFineRequestTestCase(long loanId, long accountId, long loanFinePolicyId) {
        CreateLoanFineRequest request = new CreateLoanFineRequest();
        request.setLoanId(loanId);
        request.setAmount(new BigDecimal("50.00"));
        request.setAccountId(accountId);
        request.setLoanFinePolicyId(loanFinePolicyId);
        request.setType(LoanFineType.OVERDUE_PAYMENT);
        return request;
    }

    public UpdateLoanFineRequest getUpdateLoanFineRequestTestCase(long id) {
        UpdateLoanFineRequest request = new UpdateLoanFineRequest();
        request.setId(id);
        request.setAmount(new BigDecimal("75.00"));
        request.setType(LoanFineType.EARLY_PAYMENT);
        return request;
    }

    public LoanFine getLoanFineTestCase(Loan loan) {
        LoanFine loanFine = new LoanFine();
        loanFine.setId(1L);
        loanFine.setLoan(loan);
        loanFine.setAmount(new BigDecimal("50.00"));
        loanFine.setType(LoanFineType.OVERDUE_PAYMENT);
        loanFine.setCreatedAt(LocalDate.now());
        return loanFine;
    }

    public GetLoanFineResponse getLoanFineResponseTestCase() {
        Loan loan = getLoanTestCase();
        LoanFine loanFine = getLoanFineTestCase(loan);
        GetLoanFineResponse response = new GetLoanFineResponse();
        response.setId(loanFine.getId());
        response.setLoanId(loanFine.getLoan().getId());
        response.setAmount(loanFine.getAmount());
        response.setCreatedAt(loanFine.getCreatedAt());
        response.setType(loanFine.getType());
        return response;
    }
}
