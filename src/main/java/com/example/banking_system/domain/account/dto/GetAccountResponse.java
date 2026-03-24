package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountStatus;
import com.example.banking_system.domain.account.constant.AccountType;
import lombok.Data;

@Data
public class GetAccountResponse {
    private long id;
    private String email;
    private String phoneNumber;
    private String number;
    private String address;
    private AccountType type;
    private AccountStatus status;
}
