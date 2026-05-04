package com.example.banking_system.common.Controller;

import com.example.banking_system.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Banking System API!";
    }

    @GetMapping("/error")
    public void error() {
        throw new ValidationException("Test validation error");
    }

}
