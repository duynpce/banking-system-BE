package com.example.banking_system.account.dto;

import com.example.banking_system.account.constant.AccountType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class CreateAccountRequest {
    @Setter(lombok.AccessLevel.NONE)
    private final AccountType type;

    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "email cannot be blank")
    @Email(message = "email should be valid")
    private String email;
    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;
    @NotBlank(message = "address cannot be blank")
    private String address;

    protected CreateAccountRequest(AccountType type) {
        this.type = type;
    }

}
