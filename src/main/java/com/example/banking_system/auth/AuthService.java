package com.example.banking_system.auth;

import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.auth.dto.LoginRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.auth.dto.GetTokenResponse;
import com.example.banking_system.common.OAuthProperties;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.common.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AccountQueryService accountQueryService;
    private final OAuthProperties oAuthProperties;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;

    public long login(LoginRequest loginRequest) {
        Account account = accountQueryService.findByUsername(loginRequest.getUsername());

        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())){
            throw new UnauthorizedException("incorrect password or username");
        }

        return account.getId();
    }

    public GetTokenResponse getToken(String code, HttpServletRequest request) {
        if(code == null || code.isEmpty()){
            throw new UnauthorizedException("hasn't authenticated with the authorization server, no code provided");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oAuthProperties.getRedirectUri());

        String sessionId = request.getHeader(HttpHeaders.COOKIE);

        GetTokenResponse response = webClient.post()
                .uri(oAuthProperties.getAuthServerUri() + oAuthProperties.getTokenUri())
                .header(HttpHeaders.AUTHORIZATION,"Basic " + oAuthProperties.getPostBasicSecret())
                .header(HttpHeaders.COOKIE, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GetTokenResponse.class)
                .block();

        if(response == null){
            throw new UnauthorizedException("Failed to retrieve access token");
        }

        return response;
    }

    public GetTokenResponse refreshToken(String refreshToken, HttpSession session, HttpServletRequest request) {
        String sessionId = request.getHeader(HttpHeaders.COOKIE);
        final String username = (String) session.getAttribute("username");

        if(refreshToken == null || refreshToken.isEmpty()){
            throw new UnauthorizedException("no token provided, please login");
        }


        if(username == null){
            session.invalidate();
            throw new UnauthorizedException("User not authenticated");
        }

        if(!jwtUtil.getUsername().equals(username)){
            session.invalidate();
            throw new UnauthorizedException("invalid refresh token");
        }


        // Call the authentication service to refresh the token
        GetTokenResponse getTokenResponse = webClient.post()
                .uri( oAuthProperties.getAuthServerUri() + oAuthProperties.getTokenUri())
                .header("Authorization", "Bearer " + refreshToken)
                .header(HttpHeaders.COOKIE, sessionId)
                .retrieve()
                .bodyToMono(GetTokenResponse.class)
                .block();

        if (getTokenResponse == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        return getTokenResponse;
    }
}
