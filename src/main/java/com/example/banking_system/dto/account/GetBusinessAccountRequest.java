package com.example.banking_system.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetBusinessAccountRequest extends GetAccountRequest {
    private String organizationName;
    private String taxIdNumber;
}

