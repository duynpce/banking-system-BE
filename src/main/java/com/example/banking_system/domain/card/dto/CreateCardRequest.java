package com.example.banking_system.domain.card.dto;

import com.example.banking_system.domain.card.constant.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parent;
import org.hibernate.validator.constraints.Length;

@Data
public abstract class CreateCardRequest {

    @NotBlank(message = "Privilege code is required")
    private String privilegeCode;

    @NotNull(message = "Card type is required")
    private CardType type;

    @NotBlank(message = "Pin code must not be blank")
    @Size(min = 6, max = 6, message = "Pin code must be exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "Pin code must consist of digits only")
    private String pinCode;
}
