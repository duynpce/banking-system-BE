package com.example.banking_system.account.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "account_details")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class AccountDetails {
    @Id
    @Column(name = "account_id")
    private long accountId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
