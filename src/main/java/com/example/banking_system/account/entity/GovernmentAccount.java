package com.example.banking_system.account.entity;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.constant.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "government_account_details")
@PrimaryKeyJoinColumn(name = "account_id")
@EqualsAndHashCode(callSuper = false)
public class GovernmentAccount extends  AccountDetails {

    @Column(name = "government_department", columnDefinition = "text", nullable = false)
    private String governmentDepartment;

    public GovernmentAccount() {
        setAccount(new Account());
        getAccount().setType(AccountType.GOVERNMENT);
    }

    public GovernmentAccount(String username, String password, String email
            , String phoneNumber, String address, String governmentDepartment
    ) {
        setAccount(new Account(username, password, email, phoneNumber, address, AccountType.GOVERNMENT));
        this.governmentDepartment = governmentDepartment;
    }

}
