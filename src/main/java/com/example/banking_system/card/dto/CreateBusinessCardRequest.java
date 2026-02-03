package com.example.banking_system.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessCardRequest extends  CreateCardRequest{

    @NotBlank(message = "departmentCode must not be blank")
    private String departmentCode;
}
