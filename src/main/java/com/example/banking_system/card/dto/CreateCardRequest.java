package com.example.banking_system.card.dto;

import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.constant.CardType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class CreateCardRequest {
    @NotNull(message = "Card privilege is required")
    private CardPrivilege privilege;

    @NotNull(message = "Card type is required")
    private CardType type;
}
