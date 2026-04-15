package com.example.banking_system.domain.account.dto;

import com.example.banking_system.domain.account.constant.AccountType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class CreateAccountRequest {
    @Setter(lombok.AccessLevel.NONE)
    private final AccountType type;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "phone number cannot be blank")
    @Size(message = "phone number must be between 10 and 11 characters long", min = 10, max = 11)
    private String phoneNumber;

    @NotBlank(message = "address cannot be blank")
    @Size(min = 5 , max = 1000, message = "address must be between 5 and 1000 characters long")
    private String address;

    protected CreateAccountRequest(AccountType type) {
        this.type = type;
    }

}
