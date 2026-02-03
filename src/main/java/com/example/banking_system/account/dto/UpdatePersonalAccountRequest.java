package com.example.banking_system.account.dto;

import com.example.banking_system.account.constant.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdatePersonalAccountRequest extends UpdateAccountRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private String idCardNumber;
    private Gender gender;
}

