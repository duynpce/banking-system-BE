package com.example.banking_system.domain.account.dto;

import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateGovernmentAccountRequest extends UpdateAccountRequest {
    @Size(min = 3, max = 1000, message = "Government department must be between 3 and 1000 characters long")
    private String governmentDepartment;
}



