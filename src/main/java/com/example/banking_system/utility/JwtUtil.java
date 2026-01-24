package com.example.banking_system.utility;

import com.example.banking_system.dto.auth.TokenResponse;
import com.example.banking_system.exception.BusinessException;
import com.example.banking_system.exception.ForbiddenException;
import com.example.banking_system.exception.UnauthorizedException;
import com.example.banking_system.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class JwtUtil {

    // get username from ContextHolder
    public String getUsername(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new UnauthorizedException("haven't logged in");
        }

        return authentication.getName();
    }


}
