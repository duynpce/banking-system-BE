//package com.example.banking_system.config.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//
//import java.io.IOException;
//@Configuration
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Bean
//    LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
//        return new LoginUrlAuthenticationEntryPoint("/login");
//    }
//
//    // called when invalid token or unauthenticated
//    @Bean
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//
//    }
//}
