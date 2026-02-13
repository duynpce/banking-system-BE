package com.example.banking_system.auth;

import com.example.banking_system.account.entity.Account;
import com.example.banking_system.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        long accountID = authService.login(loginRequest);

        session.setAttribute("accountId", accountID);

        return ResponseEntity.ok("Login successful");
    }
}
