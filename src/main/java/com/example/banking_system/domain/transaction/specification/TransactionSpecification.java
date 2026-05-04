package com.example.banking_system.domain.transaction.specification;

import com.example.banking_system.domain.transaction.Transaction;
import com.example.banking_system.domain.transaction.constant.TransactionStatus;
import com.example.banking_system.domain.transaction.constant.TransactionType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public final class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> senderJoin = root.join("sender", JoinType.LEFT);
            Join<Object, Object> receiverJoin = root.join("receiver", JoinType.LEFT);

            return criteriaBuilder.or(
                    criteriaBuilder.equal(senderJoin.get("username"), username),
                    criteriaBuilder.equal(receiverJoin.get("username"), username)
            );
        };
    }

    public static Specification<Transaction> hasSenderUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join("sender", JoinType.LEFT).get("username"),
                username
        );
    }

    public static Specification<Transaction> hasReceiverUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join("receiver", JoinType.LEFT).get("username"),
                username
        );
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        if (type == null) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Transaction> hasStatus(TransactionStatus status) {
        if (status == null) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Transaction> createdAtBetween(LocalDate startDate, LocalDate endDate) {
        Instant startDateTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1).toInstant(ZoneOffset.UTC);

        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                root.get("createdAt"),
                startDateTime,
                endDateTime
        );
    }

}

