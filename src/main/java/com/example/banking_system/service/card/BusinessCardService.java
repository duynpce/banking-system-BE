package com.example.banking_system.service.card;

import com.example.banking_system.dto.card.CreateBusinessCardRequest;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.entity.card.BusinessCard;
import com.example.banking_system.repository.card.BusinessCardRepository;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.JwtUtil;
import com.example.banking_system.validator.card.BusinessCardValidator;
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

        return businessCardRepository.save(businessCard);
    }
}
