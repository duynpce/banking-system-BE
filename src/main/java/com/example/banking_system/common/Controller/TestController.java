package com.example.banking_system.common.Controller;

import com.example.banking_system.card.service.CardService;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
@RequiredArgsConstructor
public class TestController {
    private final JwtUtil jwtUtil;
    private final CardService cardService;

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Banking System API!";
    }

    @GetMapping("/jwt-get-username")
    public String testJwtGetUsername() {
        return jwtUtil.getUsername();
    }

    @GetMapping("/generate-card-number")
    public String generateCardNumber() {
        return cardService.generateCardNumber();
    }

}
