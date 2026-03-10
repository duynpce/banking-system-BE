package com.example.banking_system.card.service.domain;

import com.example.banking_system.account.entity.PersonalAccount;
import com.example.banking_system.account.service.query.AccountQueryService;
import com.example.banking_system.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.repository.PersonalCardRepository;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
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
    private final PersonalAccountQueryService personalAccountQueryService;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final JwtUtil jwtUtil;


    @Transactional
    public PersonalCard create(CreatePersonalCardRequest request) {
        String username = jwtUtil.getUsername();
        PersonalAccount personalAccount = personalAccountQueryService.findByUsername(username);
        Account account = personalAccount.getAccount();

        personalCardValidator.validateCreate(account);

        String cardNumber = cardService.generateCardNumber();

        CardPrivilege privilege = cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(request.getPrivilegeCode());
        PersonalCard personalCard = new PersonalCard(request.getPinCode(),cardNumber, personalAccount.getFullName(), request.getType(),privilege);
        personalCard.getCard().setAccount(account);
        cardService.updateExpirationDateOnCreate(personalCard.getCard());

        return personalCardRepository.save(personalCard);
    }
}
