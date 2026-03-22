package com.example.banking_system.domain.account.dto;

import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateGovernmentAccountRequest extends UpdateAccountRequest {
    private String governmentDepartment;
}



