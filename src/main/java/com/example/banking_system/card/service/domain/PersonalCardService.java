package com.example.banking_system.card.service.domain;

import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.repository.PersonalCardRepository;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.card.validator.PersonalCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalCardService {
    private final PersonalCardRepository personalCardRepository;
    private final PersonalCardValidator personalCardValidator;
    private final CardService cardService;
    private final AccountQueryService accountQueryService;
    private final JwtUtil jwtUtil;


    @Transactional
    public PersonalCard create(CreatePersonalCardRequest request) {
        String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        personalCardValidator.validateCreate(account);

        String cardNumber = cardService.generateCardNumber();

        PersonalCard personalCard = new PersonalCard(cardNumber, request.getType(), request.getPrivilege());
        personalCard.getCard().setAccount(account);
        cardService.updateExpirationDateOnCreate(personalCard.getCard());

        return personalCardRepository.save(personalCard);
    }
}
