package com.example.banking_system.domain.card.service.domain;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.mapper.CardPrivilegeMapper;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.domain.card.validator.CardPrivilegeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardPrivilegeService {
    private final CardPrivilegeValidator cardPrivilegeValidator;
    private final CardPrivilegeMapper cardPrivilegeMapper;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
    private final CardPrivilegeCodeQueryService cardPrivilegeCodeQueryService;

    public CardPrivilege create(CreateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = cardPrivilegeMapper.toEntity(request);
        cardPrivilegeValidator.validateCreate(cardPrivilege);

        String normalizedCode = request.getCode().toUpperCase(Locale.ROOT);
        CardPrivilegeCode cardPrivilegeCode  = cardPrivilegeCodeQueryService.findByCodeAndIsActive(normalizedCode);
        cardPrivilege.setCardPrivilegeCode(cardPrivilegeCode);
        return cardPrivilegeQueryService.save(cardPrivilege);
    }

    public CardPrivilege update(UpdateCardPrivilegeRequest request) {
        String normalizedCode = request.getCode().toUpperCase(Locale.ROOT);
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(normalizedCode);
        cardPrivilegeValidator.validateUpdate(request, cardPrivilege);
        return cardPrivilegeQueryService.save(cardPrivilege);
    }

    @Transactional(readOnly = true)
    public GetCardPrivilegeResponse getById(long id) {
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findById(id);
        return cardPrivilegeMapper.toDto(cardPrivilege);
    }

    @Transactional(readOnly = true)
    public GetCardPrivilegeResponse getByCodeAndIsActive(String code) {
        String normalizedCode = code.toUpperCase(Locale.ROOT);
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findByPrivilegeCodeAndIsActive(normalizedCode);
        return cardPrivilegeMapper.toDto(cardPrivilege);
    }

    @Transactional(readOnly = true)
    public List<GetCardPrivilegeResponse> getAll() {
        List<CardPrivilege> cardPrivilegeList = cardPrivilegeQueryService.findAll();
        return cardPrivilegeMapper.toDtoList(cardPrivilegeList);
    }

    @Transactional(readOnly = true)
    public List<GetCardPrivilegeResponse> getByPage(int page, int limit) {
        Page<CardPrivilege> cardPrivilegePage = cardPrivilegeQueryService.findAllWithPagination(page, limit);
        return cardPrivilegeMapper.toDtoList(cardPrivilegePage.getContent());
    }


}
