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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> findByFromAccountAndDateRange(String username, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("startDate must be before or equal to endDate");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return transactionRepository.findByFromAccount_UsernameAndCreatedAtBetween(
                username,
                startDateTime,
                endDateTime
        );
    }

    public Page<Transaction> findByFromAccount(String username, Integer page, Integer limit) {
        if (page < 0) {
            throw new ValidationException("page must be greater than or equal to 0");
        }
        if (limit <= 0) {
            throw new ValidationException("limit must be greater than 0");
        }

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return transactionRepository.findByFromAccount_Username(username, pageable);
    }
}
