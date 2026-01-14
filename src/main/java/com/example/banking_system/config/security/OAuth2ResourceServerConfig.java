package com.example.banking_system.config.security;

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
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2ResourceServerConfig {


        // Resource Server Security Filter Chain
        @Bean
//        @Order(3)
        @Order(2)
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                        .securityMatcher("/v1/**")
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/v1/auth/**", "/v1/home" ,"/v1/author-endpoints").permitAll()
                                .requestMatchers(HttpMethod.POST, "/v1/business-accounts","/v1/personal-accounts").permitAll()
                                .anyRequest().authenticated()

                        )
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                ;
                return httpSecurity.build();

        }

}
