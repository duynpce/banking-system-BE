package com.example.banking_system.card.mapper;

import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.dto.GetBusinessCardResponse;
import com.example.banking_system.card.dto.GetPersonalCardResponse;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.PersonalCard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    List<GetPersonalCardResponse> toPersonalCardsDto(List<Card> cards);
    List<GetBusinessCardResponse> toBusinessCardsDto(List<Card> cards);
    GetPersonalCardResponse toPersonalCardDto(PersonalCard personalCard);
    GetBusinessCardResponse toBusinessCardDto(BusinessCard businessCard);
    PersonalCard toEntity(CreatePersonalCardRequest createPersonalCardRequest);
    BusinessCard toEntity(CreateBusinessAccountRequest createBusinessAccountRequest);
}
