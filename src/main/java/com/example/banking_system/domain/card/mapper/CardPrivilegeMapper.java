package com.example.banking_system.domain.card.mapper;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardPrivilegeMapper {
    CardPrivilege toEntity(CreateCardPrivilegeRequest request);
    CardPrivilege toEntity(UpdateCardPrivilegeRequest request);
    GetCardPrivilegeResponse toDto(CardPrivilege cardPrivilege);
    List<GetCardPrivilegeResponse> toDtoList(List<CardPrivilege> cardPrivilegeList);
}
