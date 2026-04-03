package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.account.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatePersonalAccountRequest extends CreateAccountRequest{
    @NotBlank(message = "full name cannot be blank")
    @Size(min = 3, max = 1000, message = "full name must be between 3 and 1000 characters long")
    private String fullName;

    @NotBlank(message = "id card number cannot be blank")
    @Size(min = 9, max = 15, message = "id card number must be between 9 and 15 characters long")
    private String idCardNumber;

    @NotNull(message = "date of birth cannot null")
    private LocalDate dateOfBirth;

    @NotNull(message = "gender cannot be null")
    private Gender gender;  

    public CreatePersonalAccountRequest() {
        super(AccountType.PERSONAL);
    }
}
