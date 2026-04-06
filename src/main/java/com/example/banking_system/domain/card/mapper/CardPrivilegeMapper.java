package com.example.banking_system.domain.card.mapper;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Locale;

@Mapper(componentModel = "spring", imports = Locale.class)
public interface CardPrivilegeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java(request.getCode() == null ? null : request.getCode().toUpperCase(Locale.ROOT))")
    CardPrivilege toEntity(CreateCardPrivilegeRequest request);

    CardPrivilege toEntity(UpdateCardPrivilegeRequest request);

    GetCardPrivilegeResponse toDto(CardPrivilege cardPrivilege);

    List<GetCardPrivilegeResponse> toDtoList(List<CardPrivilege> cardPrivilegeList);
}
