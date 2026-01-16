package com.example.banking_system.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetGovernmentAccountRequest extends GetAccountRequest {
    private String governmentDepartment;
}

