package com.example.banking_system.controller;

import com.example.banking_system.dto.auth.LoginRequest;
import com.example.banking_system.dto.auth.TokenResponse;
import com.example.banking_system.dto.common.ResponseDto;
import com.example.banking_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        ResponseDto<TokenResponse> responseDto = new ResponseDto<>("Login successful", tokenResponse);
        return ResponseEntity.ok(responseDto);
    }
}
