package com.example.banking_system.account.dto;

import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateGovernmentAccountRequest extends UpdateAccountRequest {
    private String governmentDepartment;
}



