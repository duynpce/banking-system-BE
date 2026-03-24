package com.example.banking_system.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessCardRequest extends  CreateCardRequest{

    @NotBlank(message = "cardHolder must not be blank")
    private String holder;

    public  CreateBusinessCardRequest() {

    }
}
