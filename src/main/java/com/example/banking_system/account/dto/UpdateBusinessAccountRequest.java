package com.example.banking_system.account.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateBusinessAccountRequest extends UpdateAccountRequest{
    private String organizationName;
    private String taxIdNumber;
}

