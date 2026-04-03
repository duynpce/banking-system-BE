package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessAccountRequest extends CreateAccountRequest {

    @NotBlank(message = "Organization name cannot be blank")
    @Size(min = 3, max = 1000, message = "Organization name must be between 3 and 1000 characters long")
    private String organizationName;

    @NotBlank(message = "Tax ID number cannot be blank")
    @Size(min = 10, max = 15, message = "Tax ID number must be between 10 and 15 characters long")
    private String taxIdNumber;

    public CreateBusinessAccountRequest() {
        super(AccountType.BUSINESS);
    }


}
