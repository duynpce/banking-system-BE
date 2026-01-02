package com.example.banking_system.entity.account;

import com.example.banking_system.constant.AccountStatus;
import com.example.banking_system.constant.AccountType;
import com.example.banking_system.constant.CreditRank;
import com.example.banking_system.constant.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = false, columnDefinition = "text")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    private Role role;

    @Column(name ="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @Column(name = "credit_rating", nullable = false)
    private int creditRating;

    @Column(name = "verified_email_at")
    private Instant verifiedEmailAt;

    @Column(name = "verified_phone_number_at")
    private Instant verifiedPhoneNumberAt;

    @Column(name = "verified_id_card_at")
    private Instant verifiedIdCardAt;

    // constructor for creating new account, all the non-specified fields will be set to default values in db
    public Account(String username, String password, BigDecimal balance ,String email, String phoneNumber, String address) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // when the field is not null, it means the corresponding info is verified at a specific time
    public boolean isEmailVerified() {
        return verifiedEmailAt != null;
    }

    public boolean isPhoneNumberVerified() {
        return verifiedPhoneNumberAt != null;
    }

    public boolean isIdCardVerified() {
        return verifiedIdCardAt != null;
    }

    public CreditRank getCreditRank(){
        if (creditRating >= 800) {return CreditRank.EXCELLENT;}
        else if (creditRating >= 700) {return CreditRank.GOOD;}
        else if (creditRating >= 600) {return CreditRank.FAIR;}
        else {return CreditRank.POOR;}
    }

    public abstract AccountType getType();
}
