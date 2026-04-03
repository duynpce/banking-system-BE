package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdatePersonalAccountRequest extends UpdateAccountRequest {
    @Size(min = 3, max = 1000, message = "full name must be between 3 and 1000 characters long")
    private String fullName;

    private LocalDate dateOfBirth;

    @Size(min = 9, max = 15, message = "id card number must be between 9 and 15 characters long")
    private String idCardNumber;

    private Gender gender;
}

