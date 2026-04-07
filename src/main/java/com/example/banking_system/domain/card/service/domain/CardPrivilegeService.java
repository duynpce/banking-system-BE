package com.example.banking_system.domain.card.service.domain;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
import com.example.banking_system.domain.card.dto.CreateCardPrivilegeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.mapper.CardPrivilegeMapper;
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

    @Transactional
    public CardPrivilege create(CreateCardPrivilegeRequest request) {
        request.setCode(request.getCode().toUpperCase(Locale.ROOT));
        CardPrivilege cardPrivilege = cardPrivilegeMapper.toEntity(request);
        cardPrivilegeValidator.validateCreate(cardPrivilege);
        return cardPrivilegeQueryService.save(cardPrivilege);
    }

    @Transactional
    public CardPrivilege update(UpdateCardPrivilegeRequest request) {
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findById(request.getId());
        cardPrivilegeValidator.validateUpdate(request, cardPrivilege);
        return cardPrivilegeQueryService.save(cardPrivilege);
    }

    @Transactional(readOnly = true)
    public GetCardPrivilegeResponse getById(long id) {
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findById(id);
        return cardPrivilegeMapper.toDto(cardPrivilege);
    }

    @Transactional(readOnly = true)
    public GetCardPrivilegeResponse getByCodeAndAccountTypeAndCardTypeAndIsActive(String code, AccountType accountType, CardType cardType) {
        String normalizedCode = code.toUpperCase(Locale.ROOT);
        CardPrivilege cardPrivilege = cardPrivilegeQueryService.findByCodeAndAccountTypeAndCardTypeAndIsActive(normalizedCode, accountType, cardType);
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
