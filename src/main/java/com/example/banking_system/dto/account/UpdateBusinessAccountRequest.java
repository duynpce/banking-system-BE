package com.example.banking_system.dto.account;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateBusinessAccountRequest extends UpdateAccountRequest{
    private String organizationName;
    private String taxIdNumber;
}

