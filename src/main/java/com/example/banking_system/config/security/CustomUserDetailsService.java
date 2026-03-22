package com.example.banking_system.config.security;

import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountQueryService accountQueryService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Account account = accountQueryService.findByUsername(username);
            return User.builder()
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .roles(account.getRole().name())
                    .build();
        }catch(NotFoundException e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
