package com.example.banking_system.domain.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EditPasswordRequest {

    @NotBlank(message = "Old password cannot be blank")
    private String currentPassword;

    @NotBlank(message = "New password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "New password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String newPassword;
}

