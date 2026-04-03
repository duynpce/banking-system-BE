package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateGovernmentAccountRequest extends CreateAccountRequest{
    @NotBlank(message = "Government department cannot be blank")
    @Size(min = 3 , max = 1000, message = "Government department must be between 3 and 1000 characters long")
    private String governmentDepartment;

    public CreateGovernmentAccountRequest() {
        super(AccountType.GOVERNMENT);
    }

}
