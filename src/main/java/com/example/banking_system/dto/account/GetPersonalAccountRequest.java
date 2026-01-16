package com.example.banking_system.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetPersonalAccountRequest extends GetAccountRequest {
    private String fullName;
    private String idCardNumber;
    private LocalDate dateOfBirth;
}

