package com.example.banking_system.entity.account;

import com.example.banking_system.constant.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "business_account")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class BusinessAccount extends Account{

    @Column(name = "organization_name", columnDefinition = "text")
    private String organizationName;

    @Column(name = "tax_id_number", nullable = false, unique = true)
    private String taxIdNumber;

    public BusinessAccount() {
        super();
    }

    public BusinessAccount(String username, String password, BigDecimal balance, String email
            , String phoneNumber, String address, String organizationName, String taxIdNumber
    ) {
        super(username, password, balance, email, phoneNumber, address);
        this.organizationName = organizationName;
        this.taxIdNumber = taxIdNumber;
    }

    @Override
    public AccountType getType() {
        return AccountType.BUSINESS;
    }
}
