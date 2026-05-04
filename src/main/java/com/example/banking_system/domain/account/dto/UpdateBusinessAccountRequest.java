package com.example.banking_system.domain.account.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateBusinessAccountRequest extends UpdateAccountRequest{
    @Size(min = 3, max = 1000, message = "Organization name must be between 3 and 1000 characters long")
    private String organizationName;

    @Size(min = 10, max = 15, message = "Tax ID number must be between 10 and 15 characters long")
    private String taxIdNumber;
}

