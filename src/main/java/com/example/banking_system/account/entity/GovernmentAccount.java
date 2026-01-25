package com.example.banking_system.account.entity;

import com.example.banking_system.account.constant.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "government_account")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class GovernmentAccount extends Account {

    @Column(name = "government_department", columnDefinition = "text", nullable = false)
    private String governmentDepartment;

    public  GovernmentAccount() {
        super();
    }

    public GovernmentAccount(String username, String password, String email
            , String phoneNumber, String address, String governmentDepartment
            ) {
        super(username, password, email, phoneNumber, address);
        this.governmentDepartment = governmentDepartment;
    }

    @Override
    public AccountType getType() {
        return AccountType.GOVERNMENT;
    }
}
