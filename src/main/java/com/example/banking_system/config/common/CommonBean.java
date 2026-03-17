package com.example.banking_system.config.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class CommonBean {

    @Bean
    public SecureRandom  secureRandom() {return new SecureRandom();}
}
