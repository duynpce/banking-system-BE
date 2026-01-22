package com.example.banking_system.dto.card;

import com.example.banking_system.constant.CardType;
import com.example.banking_system.constant.Privilege;
import lombok.Data;

@Data
public abstract class GetCardResponse {
    private long id;
    private String cardNumber;
    private String expirationDate;
    private CardType type;
    private Privilege privilege;
}
