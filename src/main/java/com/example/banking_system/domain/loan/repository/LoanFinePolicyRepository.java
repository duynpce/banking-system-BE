package com.example.banking_system.domain.loan.repository;

import com.example.banking_system.domain.loan.constant.LoanFineType;
import com.example.banking_system.domain.loan.constant.LoanType;
import com.example.banking_system.domain.loan.entity.LoanFinePolicy;
import com.example.banking_system.domain.loan.entity.LoanPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanFinePolicyRepository extends JpaRepository<LoanFinePolicy, Long> {

    List<LoanFinePolicy> findByLoanFineTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LoanFineType loanFineType,
            LocalDate effectiveFrom,
            LocalDate effectiveTo
    );

    default List<LoanFinePolicy> findByLoanFineTypeAndDate(LoanFineType loanFineType, LocalDate date) {
        return findByLoanFineTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(loanFineType, date, date);
    }

    boolean existsByLoanFineTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            LoanFineType loanFineType,
            LocalDate effectiveTo,
            LocalDate effectiveFrom
    );

    default boolean hasOverlap(LoanFineType loanFineType, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return existsByLoanFineTypeAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                loanFineType, effectiveTo, effectiveFrom
        );
    }
}

