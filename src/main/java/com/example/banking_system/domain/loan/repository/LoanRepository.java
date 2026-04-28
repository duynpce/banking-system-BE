package com.example.banking_system.domain.loan.repository;

import com.example.banking_system.domain.loan.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @EntityGraph(attributePaths = {"account"})
    Optional<Loan> findByIdAndAccountId(long loanId, long accountId);

    Page<Loan> findByAccountId(long accountId, Pageable pageable);
}

