package com.example.banking_system.domain.loan.validator;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.dto.RepayLoanRequest;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import org.springframework.stereotype.Component;

@Component
public class LoanValidator {

    public void validateCreate(Loan loan, LoanPolicy loanPolicy) {
        if (loan.getType() != loanPolicy.getLoanType()) {
            throw new ValidationException("loan type must match loan policy type");
        }

        if (loan.getTotalAmount().compareTo(loanPolicy.getMaxAmount()) > 0) {
            throw new ValidationException("loan amount cannot be greater than maximum allowed by policy");
        }
    }

    public void validateRepay(Loan loan, RepayLoanRequest request , Account account) {
        if(loan.getAccount().getBalance().compareTo(account.getBalance()) < 0) {
            throw new ValidationException("account balance is insufficient to repay the loan");
        }

        if(request.getAmount().compareTo(loan.getLeftAmount()) > 0) {
            throw new ValidationException("repayment amount cannot be greater than the remaining loan amount");
        }

        if(loan.getStatus() == LoanStatus.DONE_PAYMENT){
            throw new ValidationException("loan is already fully repaid");
        }
    }

}


