package com.example.banking_system.account.dto;

import com.example.banking_system.account.constant.AccountStatus;
import com.example.banking_system.account.constant.AccountType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetAccountResponse {
    private long id;
    private String email;
    private String phoneNumber;
    private String address;
    private AccountType type;
    private AccountStatus status;
}
