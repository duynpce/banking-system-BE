package com.example.banking_system.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatePersonalAccountRequest extends CreateAccountRequest{
    @NotBlank(message = "full name cannot be blank")
    private String fullName;
    @NotBlank(message = "id card number cannot be blank")
    private String idCardNumber;
    @NotBlank(message = "date of birth cannot be blank")
    private LocalDate dateOfBirth;
}
