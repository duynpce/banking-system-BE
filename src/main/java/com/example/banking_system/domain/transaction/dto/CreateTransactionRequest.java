package com.example.banking_system.domain.transaction.dto;

import com.example.banking_system.domain.transaction.constant.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    @Length(min = 12 , max = 12, message = "destination's account number must be exactly 12 digits")
    private String receiverAccountNumber;

    @NotBlank(message = "description must not be blank")
    @Length(min = 10, message = "description must be at least 10 characters long")
    private String description;

    @NotNull
    @Min(value = 1, message = "transferred amount must be at least 1")
    private BigDecimal transferredAmount;

    @NotNull(message = "transaction type is required")
    private TransactionType type;
}
