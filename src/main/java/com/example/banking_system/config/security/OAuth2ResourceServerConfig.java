package com.example.banking_system.config.security;

import com.example.banking_system.common.OAuthProperties;
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
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OAuth2ResourceServerConfig {
        private final AuthenticationEntryPoint authenticationEntryPoint;
        private final OAuthProperties oAuthProperties;
        private final CorsConfigurationSource corsConfigurationSource;

        // Resource Server Security Filter Chain
        @Bean
        @Order(3)
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                        .sessionManagement(session -> session
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        )
                        .securityMatcher("/v1/**")
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/v1/auth/**","/v1/test/**","/v1/accounts/exists/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/v1/business-accounts","/v1/personal-accounts", "v1/government-accounts").permitAll()
                                .anyRequest().authenticated()

                        )
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint(authenticationEntryPoint)
                        )
                        .csrf(AbstractHttpConfigurer::disable)
                        .cors( cors -> cors.configurationSource(corsConfigurationSource));
                return httpSecurity.build();

        }

}
