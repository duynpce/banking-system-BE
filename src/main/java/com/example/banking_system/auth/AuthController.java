package com.example.banking_system.auth;

import com.example.banking_system.auth.dto.LoginRequest;
import com.example.banking_system.auth.dto.GetTokenResponse;
import com.example.banking_system.common.OAuthProperties;
import com.example.banking_system.common.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/callback")
    public ResponseEntity<String> oauth2Callback(@NotBlank(message = "code cannot be blank") @RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        GetTokenResponse getTokenResponse = authService.getToken(code, request);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", getTokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true) // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity
                .ok(getTokenResponse.getAccessToken());

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue("refreshToken") String refreshToken
            , HttpServletResponse response, HttpSession session, HttpServletRequest request) {
        GetTokenResponse getTokenResponse = authService.refreshToken(refreshToken, session, request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", getTokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true) // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(getTokenResponse.getAccessToken());
    }
}
