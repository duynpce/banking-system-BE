package com.example.banking_system.domain.transaction.service;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> findByUsernameAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("startDate must be before or equal to endDate");
        }

        Instant startDateTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC);

        return transactionRepository.findByUsernameAndCreatedAtBetween(
                username,
                startDateTime,
                endDateTime
        );
    }

    public Page<Transaction> findByUsernameWithPagination(String username, int page, int limit) {
        if (page < 0) {
            throw new ValidationException("page must be greater than or equal to 0");
        }
        if (limit <= 0) {
            throw new ValidationException("limit must be greater than 0");
        }

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return transactionRepository.findByUsername(username, pageable);
    }

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}
