package com.example.banking_system.domain.loan.repository;

import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanPolicyRepository extends JpaRepository<LoanPolicy, Long> {


    List<LoanPolicy> findByLoanTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LoanType loanType,
            LocalDate effectiveFrom,
            LocalDate effectiveTo
    );

    default List<LoanPolicy> findByLoanTypeAndDate(LoanType loanType, LocalDate date) {
        return findByLoanTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(loanType, date, date);
    }

    boolean existsByLoanTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LoanType loanType,
            LocalDate effectiveTo,
            LocalDate effectiveFrom
    );

    default boolean hasOverlap(LoanType loanType, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByLoanTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                loanType, effectiveTo, effectiveFrom
        );
    }

}

