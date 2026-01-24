package com.example.banking_system.service.card;

import com.example.banking_system.dto.card.CreatePersonalCardRequest;
import com.example.banking_system.entity.account.Account;
import com.example.banking_system.entity.card.PersonalCard;
import com.example.banking_system.repository.card.PersonalCardRepository;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.utility.JwtUtil;
import com.example.banking_system.validator.card.PersonalCardValidator;
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
