package com.example.banking_system.domain.loan.repository;

import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.loan.entity.LoanFine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanFineRepository extends JpaRepository<LoanFine, Long> {

    Page<LoanFine> findByAccountId(long accountId, Pageable pageable);
}

