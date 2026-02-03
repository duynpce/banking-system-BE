package com.example.banking_system.card.mapper;

import com.example.banking_system.card.dto.CreateBusinessCardRequest;
import com.example.banking_system.card.dto.GetBusinessCardResponse;
import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface BusinessCardMapper {
    BusinessCard toEntity(CreateBusinessCardRequest createBusinessCardRequest);

    @Mapping(target = ".", source = "card")
    GetBusinessCardResponse toDto(BusinessCard businessCard);

    @Mapping(target=".", source="account")
    List<GetBusinessCardResponse> toDtoList(List<BusinessCard> businessCards);
}

