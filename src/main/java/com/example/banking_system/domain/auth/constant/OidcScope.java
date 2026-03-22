package com.example.banking_system.domain.auth.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OidcScope {
    ACCOUNT_READ("account:read"),
    ACCOUNT_WRITE("account:write"),
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_WRITE("transaction:write"),
    CARD_READ("card:read"),
    CARD_WRITE("card:write"),
    LOAN_READ("loan:read"),
    LOAN_WRITE("loan:write");

    private final String scope;

}
