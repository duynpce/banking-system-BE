package com.example.banking_system.domain.account.entity;

import com.example.banking_system.domain.account.constant.AccountType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "business_account_details")
@PrimaryKeyJoinColumn(name = "account_id")
@EqualsAndHashCode(callSuper = false)
public class BusinessAccount extends AccountDetails {

    @Column(name = "organization_name", columnDefinition = "text", nullable = false)
    private String organizationName;

    @Column(name = "tax_id_number", nullable = false, unique = true)
    private String taxIdNumber;

    public BusinessAccount() {
        setAccount(new Account());
        getAccount().setType(AccountType.BUSINESS);
    }

    public BusinessAccount(String username, String password, String email
            , String phoneNumber, String address, String organizationName, String taxIdNumber
    ) {
        setAccount(new Account(username, password, email, phoneNumber, address, AccountType.BUSINESS));
        this.organizationName = organizationName;
        this.taxIdNumber = taxIdNumber;
    }

}
