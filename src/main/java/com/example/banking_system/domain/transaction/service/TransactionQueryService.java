package com.example.banking_system.domain.transaction.service;

import com.example.banking_system.common.exception.ValidationException;
import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.TransactionRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public Page<Transaction> findByFilter(String username, TransactionFilter transactionFilter) {
        if (transactionFilter.getPaginationDto() == null) {
            throw new ValidationException("pagination dto is required");
        }

        if (transactionFilter.getPaginationDto().getPage() == null || transactionFilter.getPaginationDto().getPage() < 0) {
            throw new ValidationException("page cannot be negative");
        }

        if (transactionFilter.getPaginationDto().getLimit() == null || transactionFilter.getPaginationDto().getLimit() <= 0) {
            throw new ValidationException("limit must be greater than 0");
        }

        if (transactionFilter.getTransactionGroup() == null) {
            throw new ValidationException("transaction group is required");
        }

        if (transactionFilter.getStartDate() != null && transactionFilter.getEndDate() == null) {
            throw new ValidationException("endDate is required when startDate is provided");
        }

        if (transactionFilter.getStartDate() != null && transactionFilter.getStartDate().isAfter(transactionFilter.getEndDate())) {
            throw new ValidationException("startDate must be before or equal to endDate");
        }

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

    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}
