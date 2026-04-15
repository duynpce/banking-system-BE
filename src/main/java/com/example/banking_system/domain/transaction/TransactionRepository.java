package com.example.banking_system.domain.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySender_UsernameAndCreatedAtBetweenOrReceiver_UsernameAndCreatedAtBetween(
            String fromUsername,
            Instant fromStartDateTime,
            Instant fromEndDateTime,
            String toUsername,
            Instant toStartDateTime,
            Instant  toEndDateTime
    );

    default List<Transaction> findByUsernameAndCreatedAtBetween(
            String username,
            Instant startDateTime,
            Instant endDateTime
    ) {
        return findBySender_UsernameAndCreatedAtBetweenOrReceiver_UsernameAndCreatedAtBetween(
                username, startDateTime, endDateTime,
                username, startDateTime, endDateTime
        );
    }

    Page<Transaction> findBySender_UsernameOrReceiver_Username(
            String fromUsername,
            String toUsername,
            Pageable pageable
    );

    default Page<Transaction> findByUsername(String username, Pageable pageable) {
        return findBySender_UsernameOrReceiver_Username(username, username, pageable);
    }
}
