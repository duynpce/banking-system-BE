package com.example.banking_system.domain.card.dto;

import com.example.banking_system.domain.card.constant.CardType;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetCardResponse {
    private Long id;
    private String number;
    private String expirationDate;
    private CardType type;
    private String privilege;
    private String holder;
    private BigDecimal balance;
}
