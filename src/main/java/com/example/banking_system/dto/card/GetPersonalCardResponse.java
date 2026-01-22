package com.example.banking_system.dto.card;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetPersonalCardResponse extends GetCardResponse {
    private String ownerName;
}
