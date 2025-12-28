package com.example.banking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "balance", nullable = false)
    private long balance;

    @Column(name ="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "status")
    private String status;

    @Column(name = "credit_rating", nullable = false)
    private int creditRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private String type;






}
