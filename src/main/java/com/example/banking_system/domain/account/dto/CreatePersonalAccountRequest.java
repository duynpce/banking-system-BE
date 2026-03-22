package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.account.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "date of birth cannot null")
    private LocalDate dateOfBirth;
    @NotNull(message = "gender cannot be null")
    private Gender gender;

    public CreatePersonalAccountRequest() {
        super(AccountType.PERSONAL);
    }
}
