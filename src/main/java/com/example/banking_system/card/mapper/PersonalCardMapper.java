package com.example.banking_system.card.mapper;

import com.example.banking_system.card.dto.CreatePersonalCardRequest;
import com.example.banking_system.card.dto.GetBusinessCardResponse;
import com.example.banking_system.card.dto.GetPersonalCardResponse;
import com.example.banking_system.card.entity.Card;
import com.example.banking_system.card.entity.PersonalCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface PersonalCardMapper {
    PersonalCard toEntity(CreatePersonalCardRequest createPersonalCardRequest);

    @Mapping(target = ".", source = "card")
    GetPersonalCardResponse toDto(PersonalCard personalCard);

    @Mapping(target=".", source="account")
    List<GetPersonalCardResponse> toDtoList(List<PersonalCard>  PersonalCards);

}
