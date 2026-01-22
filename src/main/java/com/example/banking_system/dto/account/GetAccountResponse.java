package com.example.banking_system.dto.account;

import com.example.banking_system.constant.AccountStatus;
import com.example.banking_system.constant.AccountType;
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
