package com.example.banking_system.controller;

import com.example.banking_system.dto.auth.LoginRequest;
import com.example.banking_system.dto.auth.TokenResponse;
import com.example.banking_system.dto.common.ResponseDto;
import com.example.banking_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
        return ResponseEntity.ok("Login successful");
    }
}
