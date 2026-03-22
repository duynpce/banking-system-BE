package com.example.banking_system.domain.account.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public abstract class UpdateAccountRequest {
    @Email(message = "email should be valid")
    private String email;
    private String phoneNumber;
    private String address;
}
