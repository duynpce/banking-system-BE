package com.example.banking_system.domain.auth;

import com.example.banking_system.domain.auth.dto.GetTokenResponse;
import com.example.banking_system.common.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<ResponseDto<GetTokenResponse>> oauth2Callback(@NotBlank(message = "code cannot be blank") @RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        GetTokenResponse getTokenResponse = authService.getToken(code, request);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", getTokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true) // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();


        getTokenResponse.setRefreshToken(null);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity
                .ok(ResponseDto.success(getTokenResponse, "login successful"));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto<String>> refreshToken(@CookieValue("refreshToken") String refreshToken
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

        return ResponseEntity.ok(ResponseDto.success(getTokenResponse.getAccessToken(), "token refreshed successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<String>> logout(@CookieValue("refreshToken") String refreshToken, HttpSession session, HttpServletRequest request, HttpServletResponse response) {

        ResponseEntity<ResponseDto<String>> responseEntity = authService.logout(refreshToken, request);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(true) // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(0)
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return responseEntity;
    }


}
