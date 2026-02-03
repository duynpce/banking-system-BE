package com.example.banking_system.account.dto;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatePersonalAccountRequest extends CreateAccountRequest{
    {
        setType(AccountType.PERSONAL);
    }
    @NotBlank(message = "full name cannot be blank")
    private String fullName;
    @NotBlank(message = "id card number cannot be blank")
    private String idCardNumber;
    @NotBlank(message = "date of birth cannot be blank")
    private LocalDate dateOfBirth;
    @NotNull(message = "gender cannot be null")
    private Gender gender;
}
