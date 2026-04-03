package com.example.banking_system.domain.card.service.domain;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.mapper.CardPrivilegeCodeMapper;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.domain.card.validator.CardPrivilegeCodeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CardPrivilegeCodeService {

    private final CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;
    private final CardPrivilegeCodeMapper cardPrivilegeCodeMapper;
    private final CardPrivilegeCodeValidator cardPrivilegeCodeValidator;

    @Transactional
    public CardPrivilegeCode create(CreateCardPrivilegeCodeRequest request) {
        CardPrivilegeCode cardPrivilegeCode = cardPrivilegeCodeMapper.toEntity(request);
        cardPrivilegeCodeValidator.validateCreate(cardPrivilegeCode);
        return cardPrivilegeCodeQueryService.save(cardPrivilegeCode);
    }

    @Transactional
    public CardPrivilegeCode update(UpdateCardPrivilegeCodeRequest request) {
        String normalizedCode = request.getCode().toUpperCase(Locale.ROOT);
        CardPrivilegeCode existingCardPrivilegeCode = cardPrivilegeCodeQueryService.findByCodeAndIsActive(normalizedCode);

        cardPrivilegeCodeValidator.validateUpdate(request, existingCardPrivilegeCode);
        return cardPrivilegeCodeQueryService.save(existingCardPrivilegeCode);
    }
}
