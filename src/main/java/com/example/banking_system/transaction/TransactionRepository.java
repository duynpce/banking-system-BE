package com.example.banking_system.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findTop1000ByFromAccount_UsernameAndCreatedAtBetweenOrToAccount_UsernameAndCreatedAtBetween(
            String fromUsername,
            LocalDateTime fromStartDateTime,
            LocalDateTime fromEndDateTime,
            String toUsername,
            LocalDateTime toStartDateTime,
            LocalDateTime toEndDateTime
    );

    default List<Transaction> findByFromAccount_UsernameAndCreatedAtBetween(
            String username,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        return findTop1000ByFromAccount_UsernameAndCreatedAtBetweenOrToAccount_UsernameAndCreatedAtBetween(
                username, startDateTime, endDateTime,
                username, startDateTime, endDateTime
        );
    }

    Page<Transaction> findByFromAccount_UsernameOrToAccount_Username(
            String fromUsername,
            String toUsername,
            Pageable pageable
    );

    default Page<Transaction> findByFromAccount_Username(String username, Pageable pageable) {
        return findByFromAccount_UsernameOrToAccount_Username(username, username, pageable);
    }
}
