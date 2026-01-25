package com.example.banking_system.card.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetBusinessCardResponse extends GetCardResponse {
    private String businessName;
}
