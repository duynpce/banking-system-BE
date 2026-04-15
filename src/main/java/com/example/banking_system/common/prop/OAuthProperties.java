package com.example.banking_system.common.prop;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "value.oauth2")
@RequiredArgsConstructor
@Getter
@Validated
public class OAuthProperties {

    @NotBlank(message = "redirect uri cannot be blank")
    private final String redirectUri;

    @NotBlank(message = "client id cannot be blank")
    private final String clientId;

    @NotBlank(message = "client secret cannot be blank")
    private final String clientSecret;

    @NotBlank(message = "scope read cannot be blank")
    private final String scopeRead;

    @NotBlank(message = "scope write cannot be blank")
    private final String scopeWrite;

    //temp
//    private final String scopeFull = scopeRead + " " + scopeWrite + " openid";

    @NotBlank(message = "authorization uri cannot be blank")
    private final String authorizationUri;

    @NotBlank(message = "token uri cannot be blank")
    private final String tokenUri;

    @NotBlank(message = "token introspection uri cannot be blank")
    private final String tokenIntrospectionUri;

    @NotBlank(message = "token revocation uri cannot be blank")
    private final String tokenRevocationUri;

    @NotBlank(message = "oidc user info uri cannot be blank")
    private final String oidcUserInfoUri;

    @NotBlank(message = "jwk set uri cannot be blank")
    private final String jwkSetUri;

    @NotBlank(message = "logout uri cannot be blank")
    private final String logoutUri;

    @NotBlank(message = "origin uri cannot be blank")
    private final String originUri;

    @NotBlank(message = "post basic secret cannot be blank")
    private final String postBasicSecret;

    @NotBlank(message = "auth server urui cannot be blank")
    private final String authServerUri;
}
