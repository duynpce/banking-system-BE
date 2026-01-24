package com.example.banking_system.dto.card;

import com.example.banking_system.constant.CardPrivilege;
import com.example.banking_system.constant.CardType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessCardRequest extends  CreateCardRequest{

}
