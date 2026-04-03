package com.example.banking_system.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessCardRequest extends  CreateCardRequest{

    @NotBlank(message = "cardHolder must not be blank")
    @Size(min = 3 , message = "cardHolder must be at least 3 characters long")
    private String holder;

    public  CreateBusinessCardRequest() {

    }
}
