package com.example.banking_system.card.service.domain;

import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.repository.BusinessCardRepository;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.card.validator.BusinessCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessCardService {
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardValidator businessCardValidator;
    private final CardService cardService;
    private final AccountQueryService accountQueryService;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final JwtUtil jwtUtil;

    @Transactional
    public BusinessCard create(CreateBusinessCardRequest request) {
        String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        businessCardValidator.validateCreate(account);

        String cardNumber = cardService.generateCardNumber();
        CardPrivilege privilege = cardPrivilegeQueryService.findByPrivilegeCode(request.getPrivilegeCode());
//        CardPrivilege privilege = cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(request.getPrivilegeCode());
        BusinessCard businessCard = new BusinessCard(request.getPinCode(), cardNumber, request.getCardHolder(), request.getType(), privilege);
        businessCard.getCard().setAccount(account);
        cardService.updateExpirationDateOnCreate(businessCard.getCard());

        return businessCardRepository.save(businessCard);
    }
}
