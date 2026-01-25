package com.example.banking_system.card.service;

import com.example.banking_system.account.constant.AccountType;
import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.account.entity.Account;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.card.mapper.CardMapper;
import com.example.banking_system.card.repository.CardRepository;
import com.example.banking_system.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final AccountService accountService;

    @Value("${value.bin}")
    private String BIN;



    public List<? extends GetCardResponse> GetAllByUsername(String username) {
        Account account = accountService.findByUsername(username);
        List<Card> cards = account.getCards();

        if(cards.isEmpty()){
            throw new NotFoundException("No cards found for user: " + username);
        }
         return mapToGetDtoList(account.getType(), cards);
    }

    public List<? extends GetCardResponse> mapToGetDtoList(AccountType accountType, List<Card> cards) {

        if (accountType== AccountType.PERSONAL) {
            return cardMapper.toPersonalCardsDto(cards);
        } else if( accountType == AccountType.BUSINESS) {
            return cardMapper.toBusinessCardsDto(cards);
        } else if(accountType == AccountType.GOVERNMENT) {
            throw new ForbiddenException("government account holders are not allowed to have cards");
        }

        throw new NotFoundException("Unknown account type");
    }
    /*
     4 steps:
        1 get username from jwt and get cardId from request param
        2 find account by username and find card by cardId
        3 compare card's account with found account
        4 if match return the card mapped to appropriate DTO
     */
    public GetCardResponse getCardByUsernameAndCardId(String username, long cardId) {
        Account account = accountService.findByUsername(username);
        Card card = findById(cardId);

        if(card.getAccount().getId() != account.getId()) {
            throw new ForbiddenException("You are not allowed to access this card");
        }

        return mapToGetDto(account.getType(), card);
    }

    private GetCardResponse mapToGetDto(AccountType accountType, Card card) {
        if (accountType == AccountType.PERSONAL) {
            return cardMapper.toPersonalCardDto((PersonalCard) card);
        } else if (accountType == AccountType.BUSINESS) {
            return cardMapper.toBusinessCardDto((BusinessCard) card);
        } else if (accountType == AccountType.GOVERNMENT) {
            throw new ForbiddenException("government account holders are not allowed to have cards");
        }

        throw new NotFoundException("Unknown account type");
    }

    /*
     4 steps:
        1 get username from jwt and get cardId from request param
        2 find account by username and find card by cardId
        3 compare card's account with found account
        4 if match delete the card
     */
    public void deleteCardByUsernameAndCardId(String username,long cardId) {
        Account account = accountService.findByUsername(username);
        Card card = findById(cardId);

        if(card.getAccount().getId() != account.getId()) {
            throw new ForbiddenException("You are not allowed to delete this card");
        }

        cardRepository.delete(card);
    }

    public Card findById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
                () -> new NotFoundException("Card not found with id: " + cardId)
        );
    }

    public String generateCardNumber() {
        String sequence = String.valueOf(cardRepository.getCardNumberSequence()).formatted("%012d");
        System.out.println(BIN + " " + sequence);
        return BIN + sequence;
    }

}
