package com.example.banking_system.card.service;

import com.example.banking_system.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.repository.BusinessCardRepository;
import com.example.banking_system.account.service.AccountService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.card.validator.BusinessCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessCardService {
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardValidator businessCardValidator;
    private final CardService cardService;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    public BusinessCard create(CreateBusinessCardRequest request) {
        String username = jwtUtil.getUsername();
        Account account = accountService.findByUsername(username);

        businessCardValidator.validateCreate(account);

        String cardNumber = cardService.generateCardNumber();

        BusinessCard businessCard = new BusinessCard(cardNumber, request.getType(), request.getPrivilege());
        businessCard.setAccount(account);
        cardService.updateAnnualFeeAndExpirationDateOnCreate(businessCard);

        return businessCardRepository.save(businessCard);
    }
}
