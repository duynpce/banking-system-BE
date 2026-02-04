package com.example.banking_system.card.mapper;

import com.example.banking_system.account.dto.CreateBusinessAccountRequest;
import com.example.banking_system.card.dto.*;
import com.example.banking_system.card.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @SubclassMapping(source = PersonalCard.class, target = GetPersonalCardResponse.class)
    @SubclassMapping(source = BusinessCard.class, target = GetBusinessCardResponse.class)
    @Mapping(target = "id",             source = "card.id")
    @Mapping(target = "cardNumber",     source = "card.cardNumber")
    @Mapping(target = "expirationDate", source = "card.expirationDate")
    @Mapping(target = "type",           source = "card.type")
    @Mapping(target = "cardPrivilege",  source = "card.privilege")
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
