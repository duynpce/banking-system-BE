package com.example.banking_system.account.dto;

import com.example.banking_system.account.constant.AccountStatus;
import com.example.banking_system.account.constant.AccountType;
import lombok.Data;

@Data
public abstract class GetAccountResponse {
    private long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private AccountType type;
    private AccountStatus status;
}
