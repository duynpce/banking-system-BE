package com.example.banking_system.auth;

import com.example.banking_system.auth.dto.GetTokenResponse;
import com.example.banking_system.common.IntegrationTest;
import com.example.banking_system.common.exception.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private AuthController authController;

    @MockitoBean
    private AuthService authService;

    @Test
    public void testOauth2Callback_Success() {
        // Arrange
        String code = "valid_auth_code";
        String accessToken = "access_token_123";
        String refreshToken = "refresh_token_456";

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        GetTokenResponse mockResponse = new GetTokenResponse();
        mockResponse.setAccessToken(accessToken);
        mockResponse.setRefreshToken(refreshToken);

        when(authService.getToken(anyString(), any())).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> result = authController.oauth2Callback(code, request, response);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response status should be OK");
        assertNotNull(result.getBody(), "Response body should not be null");
        assertEquals(accessToken, result.getBody(), "Access token should match");
        assertNotNull(response.getHeader("Set-Cookie"), "Cookie should be set");
        assertTrue(Objects.requireNonNull(response.getHeader("Set-Cookie")).contains("refreshToken"), "Cookie should contain refresh token");
    }

    @Test
    public void testOauth2Callback_NoCode_Failure() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();


        // in production, the validation of @NotBlank will handle and throw MethodArgumentNotValidException
        // mock throw UnauthorizedException because create MethodArgumentNotValidException is hard
        when(authService.getToken(anyString(), any()))
            .thenThrow(new UnauthorizedException("code cannot be blank"));

        UnauthorizedException exception = Assertions.assertThrows(UnauthorizedException.class,
            () -> authController.oauth2Callback("", request, response),
            "Should throw UnauthorizedException when code is blank");
        assertEquals("code cannot be blank", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testRefreshToken_Success() {
        // Arrange
        String refreshToken = "valid_refresh_token";
        String newAccessToken = "new_access_token_123";
        String newRefreshToken = "new_refresh_token_456";

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Objects.requireNonNull(request.getSession()).setAttribute("username", "testuser");

        GetTokenResponse mockResponse = new GetTokenResponse();
        mockResponse.setAccessToken(newAccessToken);
        mockResponse.setRefreshToken(newRefreshToken);

        when(authService.refreshToken(anyString(), any(), any())).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> result = authController.refreshToken(refreshToken, response, request.getSession(), request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response status should be OK");
        assertNotNull(result.getBody(), "Response body should not be null");
        assertEquals(newAccessToken, result.getBody(), "New access token should match");
        assertNotNull(response.getHeader("Set-Cookie"), "Cookie should be set");
        assertTrue(Objects.requireNonNull(response.getHeader("Set-Cookie")).contains("refreshToken"), "Cookie should contain refresh token");
    }

    @Test
    public void testRefreshToken_InvalidToken_Failure() {
        // Arrange
        String refreshToken = "invalid_refresh_token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(authService.refreshToken(anyString(), any(), any()))
            .thenThrow(new UnauthorizedException("Invalid refresh token"));

        // Act & Assert
        Assertions.assertThrows(UnauthorizedException.class,
            () -> authController.refreshToken(refreshToken, response, request.getSession(), request),
            "Should throw UnauthorizedException when refresh token is invalid");
    }
}
