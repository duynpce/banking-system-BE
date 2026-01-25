package com.example.banking_system.card.service;

import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.repository.PersonalCardRepository;
import com.example.banking_system.account.service.AccountService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.card.validator.PersonalCardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalCardService {
    private final PersonalCardRepository personalCardRepository;
    private final PersonalCardValidator personalCardValidator;
    private final CardService cardService;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    public PersonalCard create(CreatePersonalCardRequest request) {
        String username = jwtUtil.getUsername();
        Account account = accountService.findByUsername(username);

        personalCardValidator.validateCreate(account);

        String cardNumber = cardService.generateCardNumber();

        PersonalCard personalCard = new PersonalCard(cardNumber, request.getType(), request.getPrivilege());
        personalCard.setAccount(account);

        return personalCardRepository.save(personalCard);
    }
}
