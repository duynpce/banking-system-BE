package com.example.banking_system.card.service.domain;

import com.example.banking_system.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.entity.CardPrivilegeCode;
import com.example.banking_system.card.mapper.CardPrivilegeMapper;
import com.example.banking_system.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.card.validator.CardPrivilegeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardPrivilegeService {
    private final CardPrivilegeValidator cardPrivilegeValidator;
    private final CardPrivilegeMapper CardPrivilegeMapper;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    public CardPrivilege create(CreateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = CardPrivilegeMapper.toEntity(request);
        cardPrivilegeValidator.validateCreate(cardPrivilege);

        CardPrivilegeCode cardPrivilegeCode  = cardPrivilegeCodeQueryService.findByCode(request.getCode());
        cardPrivilege.setCardPrivilegeCode(cardPrivilegeCode);

        return cardPrivilegeQueryService.save(cardPrivilege);
    }

    public CardPrivilege update(UpdateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findByPrivilegeCode(request.getCode());
        cardPrivilegeValidator.validateUpdate(request, cardPrivilege);
        return cardPrivilegeQueryService.save(cardPrivilege);
    }

    public void deleteByPrivilegeCode(String code) {
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findByPrivilegeCode(code);
        cardPrivilegeQueryService.delete(cardPrivilege);
    }
}
