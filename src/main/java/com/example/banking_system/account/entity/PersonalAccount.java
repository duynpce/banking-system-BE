package com.example.banking_system.account.entity;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.constant.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "personal_account_details")
@PrimaryKeyJoinColumn(name = "account_id")
@EqualsAndHashCode(callSuper = false)
public class PersonalAccount extends  AccountDetails {

    @Column(name = "full_name", columnDefinition = "text")
    private String fullName;

    @Column(name = "date_of_birth", nullable = false, columnDefinition = "date")
    private LocalDate dateOfBirth;

    @Column(name = "id_card_number", nullable = false, unique = true)
    private String idCardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;


    public PersonalAccount() {
        setAccount(new Account());
        getAccount().setType(AccountType.PERSONAL);
    }

    public PersonalAccount(String username, String password, String email
            , String phoneNumber, String address, Gender gender, String fullName, LocalDate dateOfBirth
            , String idCardNumber) {
        setAccount(new Account(username, password, email, phoneNumber, address, AccountType.PERSONAL));
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.idCardNumber = idCardNumber;
        this.gender = gender;
    }

}
