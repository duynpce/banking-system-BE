package com.example.banking_system.card.mapper;

import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.dto.GetBusinessCardResponse;
import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.card.dto.GetPersonalCardResponse;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.PersonalCard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    void mapGeneralCardFields(Card card, @MappingTarget GetCardResponse getCardResponse);


}
