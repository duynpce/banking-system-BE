package com.example.banking_system.domain.account.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetGovernmentAccountResponse extends GetAccountResponse {
    private String governmentDepartment;
}

