package com.example.banking_system.domain.card.mapper;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeCodeResponse;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Locale;
import java.util.List;

@Mapper(componentModel = "spring", imports = Locale.class)
public interface CardPrivilegeCodeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java(request.getCode() == null ? null : request.getCode().toUpperCase(Locale.ROOT))")
    CardPrivilegeCode toEntity(CreateCardPrivilegeCodeRequest request);

    GetCardPrivilegeCodeResponse toDto(CardPrivilegeCode cardPrivilegeCode);

    List<GetCardPrivilegeCodeResponse> toDtoList(List<CardPrivilegeCode> cardPrivilegeCodeList);
}
