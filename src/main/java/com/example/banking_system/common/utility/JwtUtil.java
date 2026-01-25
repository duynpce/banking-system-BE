package com.example.banking_system.common.utility;

import com.example.banking_system.common.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
