package com.example.banking_system.config.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class  OAuth2ResourceServerConfig {
        private final CorsConfigurationSource corsConfigurationSource;

        // Resource Server Security Filter Chain
        @Bean
        @Order(3)
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .securityMatcher("/v1/**")
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/v1/auth/**","/v1/test/**", "/v1/*/exists/**",
                                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.POST, "/v1/business-accounts","/v1/personal-accounts", "/v1/government-accounts").permitAll()
                                .anyRequest().authenticated()

                        )
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint(resourceServerAuthenticationEntryPoint())
                        )
                        .csrf(AbstractHttpConfigurer::disable)
                        .cors( cors -> cors.configurationSource(corsConfigurationSource));
                return httpSecurity.build();

        }

        private AuthenticationEntryPoint resourceServerAuthenticationEntryPoint() {
                return (request, response, authException) -> {
                        String authHeader = request.getHeader("Authorization");
                        String message;

                        if (authHeader == null) {
                                message = "MISSING_AUTHORIZATION.";
                        }else if(!authHeader.startsWith("Bearer")){
                                message = "INVALID_AUTHORIZATION_HEADER.";
                        }
                        else {
                                message = "INVALID_OR_EXPIRED_TOKEN.";
                        }

                        //temp log for development  delete later
                        String path = request.getRequestURI();
                        log.error(message + " with the path: " + path);

                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write(message);
                        };
        }

}
