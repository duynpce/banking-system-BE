package com.example.banking_system.config.security;

import com.example.banking_system.common.OAuthProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class OAuth2AuthorizationServerConfig {

    private final PasswordEncoder passwordEncoder;
    private final OAuthProperties oAuthProperties;
    private final CorsConfigurationSource corsConfigurationSource;

    // Security Filter Chain for OAuth2 Authorization Server, used to issue tokens
    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(oAuthProperties.getLogoutUri()).permitAll()
                        .anyRequest().authenticated()
                )
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .registeredClientRepository(registeredClientRepository())
                                .oidc(oidc -> oidc.logoutEndpoint(
                                        logout -> logout.
                                                logoutResponseHandler((request, response, authentication) -> response.sendRedirect(oAuthProperties.getOriginUri() + "/login?logout=success"))
                                                .errorResponseHandler((request, response, exception) ->  response.sendRedirect(oAuthProperties.getOriginUri() + "/login?logout=error"))

                                        )
                                )
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authServerAuthenticationEntryPoint())
                )
                .csrf(AbstractHttpConfigurer::disable) //temp
                .cors(cors -> cors.configurationSource(corsConfigurationSource));

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/login")
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable) //temp
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(oAuthProperties.getOriginUri())
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl(oAuthProperties.getAuthorizationUri() + "?response_type=code&client_id="
                                + oAuthProperties.getClientId() + "&scope=" + oAuthProperties.getScopeRead() + " " +
                                oAuthProperties.getScopeWrite() + " openid"+ "&redirect_uri=" + oAuthProperties.getRedirectUri())
                        .failureHandler(customLoginFailureHandler())
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authServerAuthenticationEntryPoint())
                );

        return http.build();
    }

    private AuthenticationFailureHandler customLoginFailureHandler() {
        return (request, response, exception) -> {
            if(exception instanceof BadCredentialsException){
                response.sendRedirect(oAuthProperties.getOriginUri() +  "/login?error=invalid-credentials");
            }
            else {
                response.sendRedirect(oAuthProperties.getOriginUri() + "/login?error=authentication-failed");
            }
        };
    }

    AuthenticationEntryPoint authServerAuthenticationEntryPoint() {
        return (request, response, authException) -> response.sendRedirect(oAuthProperties.getOriginUri() + "/login");
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(oAuthProperties.getClientId())
                .clientSecret(passwordEncoder.encode(oAuthProperties.getClientSecret())) // bcrypt encoded client secret
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(oAuthProperties.getRedirectUri())
                .scope(oAuthProperties.getScopeRead())
                .scope(oAuthProperties.getScopeWrite())
                .scope("openid")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .jwkSetEndpoint(oAuthProperties.getJwkSetUri())
                .authorizationEndpoint(oAuthProperties.getAuthorizationUri())
                .tokenEndpoint(oAuthProperties.getTokenUri())
                .tokenIntrospectionEndpoint(oAuthProperties.getTokenIntrospectionUri())
                .tokenRevocationEndpoint(oAuthProperties.getTokenRevocationUri())
                .jwkSetEndpoint(oAuthProperties.getJwkSetUri())
                .oidcLogoutEndpoint(oAuthProperties.getLogoutUri())
                .oidcUserInfoEndpoint(oAuthProperties.getOicdUserInfoUri())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (selector, context) -> selector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
            return  NimbusJwtDecoder.withJwkSetUri(oAuthProperties.getAuthServerUri() + oAuthProperties.getJwkSetUri()).build();
    }

    private static RSAKey generateRsa() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            return new RSAKey.Builder(publicKey)
                    .privateKey(keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }



}
