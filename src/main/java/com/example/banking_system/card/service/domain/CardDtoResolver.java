package com.example.banking_system.card.service.domain;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.mapper.BusinessCardMapper;
import com.example.banking_system.card.mapper.PersonalCardMapper;
import com.example.banking_system.card.service.query.BusinessCardQueryService;
import com.example.banking_system.card.service.query.PersonalCardQueryService;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardDtoResolver {

    private final PersonalCardMapper personalCardMapper;
    private final BusinessCardMapper businessCardMapper;
    private final BusinessCardQueryService businessCardQueryService;
    private final PersonalCardQueryService personalCardQueryService;



    public GetCardResponse getCardAndMapToGetDto(Card card) {
        AccountType accountType = card.getAccount().getType();

        return switch (accountType) {
            case PERSONAL -> personalCardMapper.toDto(personalCardQueryService.findById(card.getId()));
            case BUSINESS -> businessCardMapper.toDto(businessCardQueryService.findById(card.getId()));
            case GOVERNMENT -> throw new ForbiddenException(
                    "government account holders are not allowed to have cards"
            );
        };
    }
    
    
    
    public List<? extends GetCardResponse> getCardsAndMapToGetDtoList(Account account) {

        AccountType accountType = account.getType();
        
        if(accountType == null) {
            throw new ValidationException("Account type is null");
        }

        return switch (accountType) {
            case PERSONAL -> {
                List<PersonalCard> cards = personalCardQueryService.getCardsFromAccountId(account.getId());
                yield personalCardMapper.toDtoList(cards);
            }
            case BUSINESS -> {
                List<BusinessCard> cards = businessCardQueryService.getCardsFromAccountId(account.getId());
                yield businessCardMapper.toDtoList(cards);
            }
            case GOVERNMENT -> throw new ForbiddenException(
                    "government account holders are not allowed to have cards"
            );
        };
    }
    
    
    
}
