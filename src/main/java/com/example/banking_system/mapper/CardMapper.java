package com.example.banking_system.mapper;

import com.example.banking_system.dto.account.CreateBusinessAccountRequest;
import com.example.banking_system.dto.card.CreatePersonalCardRequest;
import com.example.banking_system.dto.card.GetBusinessCardResponse;
import com.example.banking_system.dto.card.GetPersonalCardResponse;
import com.example.banking_system.entity.card.BusinessCard;
import com.example.banking_system.entity.card.Card;
import com.example.banking_system.entity.card.PersonalCard;
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
