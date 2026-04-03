package com.example.banking_system.domain.card.service.domain;

import com.example.banking_system.domain.account.entity.PersonalAccount;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.account.service.query.PersonalAccountQueryService;
import com.example.banking_system.domain.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.entity.PersonalCard;
import com.example.banking_system.domain.card.repository.PersonalCardRepository;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.common.utility.JwtUtil;
import com.example.banking_system.domain.card.validator.PersonalCardValidator;
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
        System.out.println(request.getPinCode());

        personalCardValidator.validateCreate(account);

        CardPrivilege privilege = cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(request.getPrivilegeCode().toUpperCase());
        String cardNumber = cardService.generateCardNumber();
        PersonalCard personalCard = new PersonalCard(request.getPinCode(),cardNumber, personalAccount.getFullName(), request.getType(),privilege);
        personalCard.getCard().setAccount(account);
        cardService.updateExpirationDateOnCreate(personalCard.getCard());

        return personalCardRepository.save(personalCard);
    }
}
