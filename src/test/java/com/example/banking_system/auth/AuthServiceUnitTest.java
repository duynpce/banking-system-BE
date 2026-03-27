package com.example.banking_system.auth;

import com.example.banking_system.domain.auth.AuthService;
import com.example.banking_system.domain.auth.dto.GetTokenResponse;
import com.example.banking_system.common.OAuthProperties;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.UnauthorizedException;
import com.example.banking_system.common.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceUnitTest extends UnitTest {

    @Mock
    OAuthProperties oAuthProperties;

    @Mock
    JwtUtil jwtUtil;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    AuthService authService;


    @Test
    public void getTokenSuccess() {
        // Given
        String code = "validCode";
        String sessionId = "sessionId";
        HttpServletRequest request = mock(HttpServletRequest.class);
        request.setAttribute(HttpHeaders.COOKIE, "SESSION=" + sessionId);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String idToken = "idToken";

        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/v1/auth/callback");
        when(oAuthProperties.getAuthServerUri()).thenReturn("http://auth-server.com");
        when(oAuthProperties.getTokenUri()).thenReturn("/oauth/token");
        when(oAuthProperties.getPostBasicSecret()).thenReturn("encodedClientCredentials");


        // Mock the WebClient behavior
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GetTokenResponse.class)).thenReturn(Mono.just(new GetTokenResponse(accessToken, refreshToken, idToken)));

        GetTokenResponse response = authService.getToken(code, request);

        assertEquals(accessToken, response.getAccessToken(), "Access token should match the expected value");
        assertEquals(refreshToken, response.getRefreshToken(), "Refresh token should match the expected value");
    }

    @Test
    public void getTokenFailsWhenResponseIsNull() {
        String code = "validCode";
        String sessionId = "sessionId";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.COOKIE)).thenReturn("SESSION=" + sessionId);

        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/v1/auth/callback");
        when(oAuthProperties.getAuthServerUri()).thenReturn("http://auth-server.com");
        when(oAuthProperties.getTokenUri()).thenReturn("/oauth/token");
        when(oAuthProperties.getPostBasicSecret()).thenReturn("encodedClientCredentials");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GetTokenResponse.class)).thenReturn(Mono.empty());

        UnauthorizedException ex = org.junit.jupiter.api.Assertions.assertThrows(
                UnauthorizedException.class,
                () -> authService.getToken(code, request)
        );
        assertEquals("Failed to retrieve access token", ex.getMessage());
    }

    @Test
    public void refreshTokenSuccess() {
        String refreshToken = "refresh";
        String username = "user";
        String sessionId = "sessionId";
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.COOKIE)).thenReturn("SESSION=" + sessionId);
        when(session.getAttribute("username")).thenReturn(username);
        when(jwtUtil.getUsername()).thenReturn(username);

        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/v1/auth/callback");
        when(oAuthProperties.getAuthServerUri()).thenReturn("http://auth-server.com");
        when(oAuthProperties.getTokenUri()).thenReturn("/oauth/token");
        when(oAuthProperties.getPostBasicSecret()).thenReturn("encodedClientCredentials");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GetTokenResponse.class))
                .thenReturn(Mono.just(new GetTokenResponse("access", "refresh", "idToken")));

        GetTokenResponse result = authService.refreshToken(refreshToken, session, request);

        assertEquals("access", result.getAccessToken());
        assertEquals("refresh", result.getRefreshToken());
    }

    @Test
    public void refreshTokenFailsWhenResponseIsNull() {
        String refreshToken = "refresh";
        String username = "user";
        String sessionId = "sessionId";
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.COOKIE)).thenReturn("SESSION=" + sessionId);
        when(session.getAttribute("username")).thenReturn(username);
        when(jwtUtil.getUsername()).thenReturn(username);

        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/v1/auth/callback");
        when(oAuthProperties.getAuthServerUri()).thenReturn("http://auth-server.com");
        when(oAuthProperties.getTokenUri()).thenReturn("/oauth/token");
        when(oAuthProperties.getPostBasicSecret()).thenReturn("encodedClientCredentials");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GetTokenResponse.class)).thenReturn(Mono.empty());

        UnauthorizedException ex = org.junit.jupiter.api.Assertions.assertThrows(
                UnauthorizedException.class,
                () -> authService.refreshToken(refreshToken, session, request)
        );
        assertEquals("Invalid refresh token", ex.getMessage());
    }
}
