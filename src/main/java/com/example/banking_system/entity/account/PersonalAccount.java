package com.example.banking_system.entity.account;

import com.example.banking_system.constant.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "personal_account")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class PersonalAccount extends  Account {
    @Column(name = "full_name", columnDefinition = "text")
    private String fullName;

    @Column(name = "date_of_birth", nullable = false, columnDefinition = "date")
    private LocalDate dateOfBirth;

    @Column(name = "id_card_number", nullable = false, unique = true)
    private String idCardNumber;

    public  PersonalAccount() {
        super();
    }


    public PersonalAccount(String username, String password, BigDecimal balance , String email
            , String phoneNumber, String address, String fullName, LocalDate dateOfBirth
            , String idCardNumber) {
        super(username, password, balance, email, phoneNumber, address);
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.idCardNumber = idCardNumber;
    }

    @Override
    public AccountType getType() {
        return AccountType.PERSONAL;
    }
}
