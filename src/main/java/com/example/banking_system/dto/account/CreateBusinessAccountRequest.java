package com.example.banking_system.dto.account;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBusinessAccountRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "Organization name cannot be blank")
    private String organizationName;
    @NotBlank(message = "Tax ID number cannot be blank")
    private String taxIdNumber;
    @NotBlank(message = "email cannot be blank")
    @Email(message = "email should be valid")
    private String email;
    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;
    @NotBlank(message = "address cannot be blank")
    private String address;
}
