package com.example.banking_system.domain.loan.repository;

import com.example.banking_system.domain.loan.dto.GetLoanReportProjection;
import com.example.banking_system.domain.loan.dto.GetLoanReportResponse;
import com.example.banking_system.domain.loan.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    @EntityGraph(attributePaths = {"account", "loan_policy"})
    Optional<Loan> findByIdAndAccountId(long loanId, long accountId);

    Page<Loan> findByAccountId(long accountId, Pageable pageable);

    @Query(value = """
            SELECT
                COALESCE(SUM(l.total_amount), 0)                          AS totalAmount,
                COALESCE(SUM(l.left_amount), 0)                           AS leftAmount,
                COALESCE(SUM((l.total_amount / p.duration_months) +
                                         (l.total_amount / p.duration_months * p.interest_rate)), 0)      AS monthlyInstallment
            FROM loan l
            JOIN loan_policy p ON l.loan_policy_id = p.id
            WHERE l.account_id = :accountId
              AND (:status IS NULL OR l.status = :status)
            """, nativeQuery = true)
    GetLoanReportProjection findReportByAccountIdAndStatus(
            @Param("accountId") long accountId,
            @Param("status") String status
    );
}

