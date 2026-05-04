package com.example.banking_system.domain.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public abstract class UpdateAccountRequest {
    @Email(message = "email should be valid")
    private String email;

    @Size(message = "phone number must be between 10 and 11 characters long", min = 10, max = 11)
    private String phoneNumber;

    @Size(min = 5, max = 1000, message = "address must be between 5 and 1000 characters long")
    private String address;
}
