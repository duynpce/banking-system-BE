package com.example.banking_system.domain.loan.specification;

import com.example.banking_system.domain.loan.constant.LoanStatus;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.entity.Loan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class LoanSpecification {

    private LoanSpecification() {
    }

    public static Specification<Loan> hasAccountId(long accountId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account").get("id"), accountId);
    }

    public static Specification<Loan> hasStatus(LoanStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Loan> hasType(LoanType loanType) {
        if (loanType == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), loanType);
    }

    public static Specification<Loan> createdAtBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }
}

