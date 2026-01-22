package com.example.banking_system.controller;

import com.example.banking_system.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HomeController {
    private final JwtUtil jwtUtil;

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Banking System API!";
    }

        @GetMapping("/test-jwt-get-username")
    public String testJwtGetUsername() {
        return jwtUtil.getUsername();
    }

}
