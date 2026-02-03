package com.example.banking_system.account.dto;

import com.example.banking_system.account.constant.AccountType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessAccountRequest extends CreateAccountRequest{
    {
        setType(AccountType.BUSINESS);
    }

    @NotBlank(message = "Organization name cannot be blank")
    private String organizationName;
    @NotBlank(message = "Tax ID number cannot be blank")
    private String taxIdNumber;

}
