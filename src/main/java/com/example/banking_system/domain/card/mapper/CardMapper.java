package com.example.banking_system.domain.card.mapper;

import com.example.banking_system.domain.card.dto.*;
import com.example.banking_system.domain.card.entity.BusinessCard;
import com.example.banking_system.domain.card.entity.Card;
import com.example.banking_system.domain.card.entity.CardDetails;
import com.example.banking_system.domain.card.entity.PersonalCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @SubclassMapping(source = PersonalCard.class, target = GetPersonalCardResponse.class)
    @SubclassMapping(source = BusinessCard.class, target = GetBusinessCardResponse.class)
    @Mapping(target = "number",     source = "card.number")
    @Mapping(target = "id",             source = "card.id")
    @Mapping(target = "expirationDate", source = "card.expirationDate")
    @Mapping(target = "type",           source = "card.type")
    @Mapping(target = "privilege",  source = "card.privilege")
    @Mapping(target = "holder",    source = "card.holder")
    GetCardResponse toDto(CardDetails details);

    List<GetCardResponse> toDtoList(List<CardDetails> detailsList);

    // ToEntity mappings: compose nested Card
    @Mapping(target = "cardId", ignore = true)
    @Mapping(target = "card",   source = "request")
    PersonalCard toEntity(CreatePersonalCardRequest request);

    @Mapping(target = "cardId", ignore = true)
    @Mapping(target = "card",   source = "request")
    BusinessCard toEntity(CreateBusinessCardRequest request);

    // Helper used for the nested mapping above; MapStruct will pick it automatically
    Card toCard(CreateCardRequest source);
}
