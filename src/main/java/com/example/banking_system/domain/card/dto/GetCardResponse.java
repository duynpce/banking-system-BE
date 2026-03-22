package com.example.banking_system.domain.card.dto;

import com.example.banking_system.domain.card.constant.CardType;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import lombok.Data;

@Data
public class GetCardResponse {
    private Long id;
    private String cardNumber;
    private String expirationDate;
    private CardType type;
    private CardPrivilege privilege;
    private String cardHolder;
}
