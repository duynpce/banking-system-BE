package com.example.banking_system.domain.card.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatePersonalCardRequest extends CreateCardRequest {
    public CreatePersonalCardRequest() {
        super(AccountType.PERSONAL);
    }
}
