package com.example.banking_system.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class OAuth2ResourceServerConfig {
        private final AuthenticationEntryPoint authenticationEntryPoint;


        // Resource Server Security Filter Chain
        @Bean
        @Order(2)
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .securityMatcher("/v1/**")
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/v1/auth/**","/v1/test/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/v1/business-accounts","/v1/personal-accounts").permitAll()
                                .anyRequest().authenticated()

                        )
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint(authenticationEntryPoint)
                        ).cors(cors ->cors.configurationSource(
                                request -> {
                                        CorsConfiguration config = new CorsConfiguration();
                                        String origin = "http://localhost:5173";
                                        config.addAllowedOriginPattern(origin);
                                        config.setAllowCredentials(true);
                                        config.addAllowedHeader("*");
                                        config.addAllowedMethod("*");
                                        config.setMaxAge(3600L * 3); // 3 hour
                                        return config;
                                })
                        );
                return httpSecurity.build();

        }

}
