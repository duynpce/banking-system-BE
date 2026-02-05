package com.example.banking_system.card.dto;

import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.constant.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class CreateCardRequest {

    @NotBlank(message = "Privilege code is required")
    private String privilegeCode;

    @NotNull(message = "Card type is required")
    private CardType type;

    @NotBlank(message = "Pin code must not be blank")
    private String pinCode;
}
