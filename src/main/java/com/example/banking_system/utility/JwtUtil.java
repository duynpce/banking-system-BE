package com.example.banking_system.utility;

import com.example.banking_system.dto.auth.TokenResponse;
import com.example.banking_system.exception.BusinessException;
import com.example.banking_system.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public SecretKey getKey(String key){
        byte [] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse generateTokens(UserDetails user) {
        final String accessToken = generateAccessToken(user);
        final String refreshToken = generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    public String generateAccessToken(UserDetails user) {
        return Jwts.builder()
                .claim(user.getUsername(),user.getAuthorities())
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getKey(accessSecret))
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .claim(user.getUsername(),user.getAuthorities())
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getKey(refreshSecret))
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token, String key) {
        return extractClaim(token, Claims::getSubject, key);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token, String key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    // Extract a specific claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String key) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    //Validate token
    public boolean validateToken(String token, UserDetails userDetails, String key) {
        final String extractedUsername = extractUsername(token, key);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token, key));
    }


    // Check if the token is expired
    private boolean isTokenExpired(String token, String key) {
        return extractExpiration(token, key).before(new Date());
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token,String key) {

        return Jwts
                .parser()
                .verifyWith(getKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        /*this is similar to
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
         */
    }

    // get username from ContextHolder
    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new ValidationException("haven't logged in");
        }

        return authentication.getName();
    }
}
