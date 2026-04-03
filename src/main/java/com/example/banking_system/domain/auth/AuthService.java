package com.example.banking_system.domain.auth;

import com.example.banking_system.common.dto.ResponseDto;
import com.example.banking_system.domain.auth.dto.GetTokenResponse;
import com.example.banking_system.common.OAuthProperties;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.common.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final OAuthProperties oAuthProperties;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;

    public GetTokenResponse getToken(String code, HttpServletRequest request) {
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

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("redirect_uri", oAuthProperties.getRedirectUri());
        formData.add("refresh_token", refreshToken);

        GetTokenResponse getTokenResponse = webClient.post()
                .uri(oAuthProperties.getAuthServerUri() + oAuthProperties.getTokenUri())
                .header(HttpHeaders.AUTHORIZATION,"Basic " + oAuthProperties.getPostBasicSecret())
                .header(HttpHeaders.COOKIE, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GetTokenResponse.class)
                .block();

        if (getTokenResponse == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        return getTokenResponse;
    }


    //temp
    //will create blacklist for access token in redis later
    public ResponseEntity<ResponseDto<String>> logout (String refreshToken, HttpServletRequest request) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new UnauthorizedException("you are not logged in");
        }

        ResponseEntity<?>  response = revokeToken(refreshToken, request);

        if(response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.ok(ResponseDto.success(null, "logout successful"));
        }

        throw new UnauthorizedException("invalid token or some error occurred, please try again");

    }

    private ResponseEntity<?> revokeToken (String refreshToken,  HttpServletRequest request) {
        String sessionId = request.getHeader(HttpHeaders.COOKIE);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token" , refreshToken);
        formData.add("token_type_hint" , "refresh_token");
        return  webClient.post().uri(oAuthProperties.getAuthServerUri() + oAuthProperties.getTokenRevocationUri())
                .header(HttpHeaders.AUTHORIZATION,"Basic " + oAuthProperties.getPostBasicSecret())
                .header(HttpHeaders.COOKIE, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .toEntity(Void.class)
                .block();
    }
}
