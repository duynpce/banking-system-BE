package com.example.banking_system.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBusinessAccountRequest extends CreateAccountRequest{
    @NotBlank(message = "Organization name cannot be blank")
    private String organizationName;
    @NotBlank(message = "Tax ID number cannot be blank")
    private String taxIdNumber;

}
