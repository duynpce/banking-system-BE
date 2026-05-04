package com.example.banking_system.domain.transaction.service;

import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.TransactionRepository;
import com.example.banking_system.domain.transaction.constant.TransactionReportType;
import com.example.banking_system.domain.transaction.dto.GetTransactionReport;
import com.example.banking_system.domain.transaction.dto.GetTransactionReportProjection;
import com.example.banking_system.domain.transaction.dto.TransactionFilter;
import com.example.banking_system.domain.transaction.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Page<Transaction> findByFilter(String username, TransactionFilter transactionFilter) {
        Pageable pageable = PageRequest.of(
                transactionFilter.getPaginationDto().getPage(),
                transactionFilter.getPaginationDto().getLimit(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Specification<Transaction> usernameSpecification = switch (transactionFilter.getTransactionGroup()) {
            case INCOME -> TransactionSpecification.hasReceiverUsername(username);
            case OUTCOME -> TransactionSpecification.hasSenderUsername(username);
            case ALL -> TransactionSpecification.hasUsername(username);
        };

        Specification<Transaction> specification = usernameSpecification
                .and(TransactionSpecification.hasType(transactionFilter.getType()))
                .and(TransactionSpecification.hasStatus(transactionFilter.getStatus()));

        if (transactionFilter.getStartDate() != null && transactionFilter.getEndDate() != null) {
            specification = specification.and(
                    TransactionSpecification.createdAtBetween(transactionFilter.getStartDate(), transactionFilter.getEndDate())
            );
        }

        return transactionRepository.findAll(specification, pageable);
    }

    public List<GetTransactionReport> getTransactionReportByAccountAndDateRange(
            long accountId,
            LocalDate startDate,
            LocalDate endDate,
            String bucket,
            TransactionReportType reportType
    ) {
        Instant startDateTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC);
        List<GetTransactionReportProjection> reportProjections = transactionRepository.findTransactionReportByAccountIdAndCreatedAtBetween(
                accountId,
                startDateTime,
                endDateTime,
                bucket
        );

        List<GetTransactionReport> reports = reportProjections.stream()
                .map(this::mapToGetTransactionReport)
                .toList();

        //set type
        reports.stream()
                .filter(Objects::nonNull)
                .forEach(report -> report.setReportType(reportType));

        return reports;

    }

    // because spring data cannot map java.sql.date to java.time.LocalDate Directly
    private GetTransactionReport mapToGetTransactionReport(GetTransactionReportProjection projection) {
        return new GetTransactionReport(
                projection.getStartDate(),
                projection.getEndDate(),
                projection.getIncomeAmount(),
                projection.getOutcomeAmount(),
                projection.getIncomeTransferAmount(),
                projection.getOutcomeTransferAmount(),
                projection.getCashbackAmount(),
                projection.getPaymentAmount(),
                projection.getDepositAmount(),
                projection.getWithdrawalAmount()
        );
    }

    public void delete(Transaction transaction) {transactionRepository.delete(transaction);
    }
}
