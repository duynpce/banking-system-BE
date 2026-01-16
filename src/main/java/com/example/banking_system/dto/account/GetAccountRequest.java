package com.example.banking_system.dto.account;

import lombok.Data;

@Data
public abstract class GetAccountRequest {
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
}
