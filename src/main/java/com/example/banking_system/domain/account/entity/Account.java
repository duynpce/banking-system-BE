package com.example.banking_system.domain.account.entity;

import com.example.banking_system.domain.account.constant.AccountStatus;
import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.account.constant.CreditRank;
import com.example.banking_system.domain.auth.constant.Role;
import com.example.banking_system.domain.card.entity.CardDetails;
import com.example.banking_system.domain.loan.entity.Loan;
import com.example.banking_system.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "account")
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_password", nullable = false, columnDefinition = "text")
    private String password;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "account_number", nullable = false, unique = true, updatable = false)
    private String accountNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = false, columnDefinition = "text")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false)
    private AccountType type;

    @Column(name ="created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    //eager mapping because we always need account details when we have an account
    @OneToOne(mappedBy = "account" , cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private AccountDetails accountDetails;

    @Column(name = "credit_rating", nullable = false)
    private int creditRating = 600;

    @Column(name = "verified_email_at")
    private Instant verifiedEmailAt = null;

    @Column(name = "verified_phone_number_at")
    private Instant verifiedPhoneNumberAt = null;

    @Column(name = "verified_id_card_number_at")
    private Instant verifiedIdCardNumberAt = null;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    public Account(String username, String password,String email, String phoneNumber, String address, AccountType type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.type = type;

    }

    // when the field is not null, it means the corresponding info is verified at a specific time
    public boolean isEmailVerified() {
        return verifiedEmailAt != null;
    }

    public boolean isPhoneNumberVerified() {
        return verifiedPhoneNumberAt != null;
    }

    public boolean isIdCardNumberVerified() {
        return verifiedIdCardNumberAt != null;
    }

    public CreditRank getCreditRank(){
        if (creditRating >= 800) {return CreditRank.EXCELLENT;}
        else if (creditRating >= 700) {return CreditRank.GOOD;}
        else if (creditRating >= 550) {return CreditRank.FAIR;}
        else {return CreditRank.POOR;}
    }

    // java
    public List<CardDetails> getCardDetailsList() {
        return this.cards.stream()
                .map(Card::getCardDetails)
                .toList();
    }

}
