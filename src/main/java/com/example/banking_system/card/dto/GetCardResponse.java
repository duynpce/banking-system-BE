package com.example.banking_system.card.dto;

import com.example.banking_system.card.constant.CardType;
import com.example.banking_system.card.entity.CardPrivilege;
import lombok.Data;

@Data
public class GetCardResponse {
    private long id;
    private String cardNumber;
    private String expirationDate;
    private CardType type;
    private CardPrivilege cardPrivilege;
    private String cardHolder;
}
