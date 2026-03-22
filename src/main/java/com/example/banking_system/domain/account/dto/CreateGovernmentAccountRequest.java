package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateGovernmentAccountRequest extends CreateAccountRequest{
    @NotBlank(message = "Government department cannot be blank")
    private String governmentDepartment;

    public CreateGovernmentAccountRequest() {
        super(AccountType.GOVERNMENT);
    }

}
