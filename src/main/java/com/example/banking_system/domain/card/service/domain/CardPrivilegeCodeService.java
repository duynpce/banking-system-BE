package com.example.banking_system.domain.card.service.domain;

import com.example.banking_system.domain.card.dto.CreateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.dto.GetCardPrivilegeCodeResponse;
import com.example.banking_system.domain.card.dto.UpdateCardPrivilegeCodeRequest;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.mapper.CardPrivilegeCodeMapper;
import com.example.banking_system.domain.card.service.query.CardPrivilegeCodeQueryService;
import com.example.banking_system.domain.card.validator.CardPrivilegeCodeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.List;

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

    @Transactional(readOnly = true)
    public GetCardPrivilegeCodeResponse getById(long id) {
        CardPrivilegeCode cardPrivilegeCode = cardPrivilegeCodeQueryService.findById(id);
        return cardPrivilegeCodeMapper.toDto(cardPrivilegeCode);
    }

    @Transactional(readOnly = true)
    public GetCardPrivilegeCodeResponse getByCodeAndIsActive(String code) {
        String normalizedCode = code.toUpperCase(Locale.ROOT);
        CardPrivilegeCode cardPrivilegeCode = cardPrivilegeCodeQueryService.findByCodeAndIsActive(normalizedCode);
        return cardPrivilegeCodeMapper.toDto(cardPrivilegeCode);
    }

    @Transactional(readOnly = true)
    public List<GetCardPrivilegeCodeResponse> getAll() {
        List<CardPrivilegeCode> cardPrivilegeCodeList = cardPrivilegeCodeQueryService.findAll();
        return cardPrivilegeCodeMapper.toDtoList(cardPrivilegeCodeList);
    }

    @Transactional(readOnly = true)
    public List<GetCardPrivilegeCodeResponse> getByPage(int page, int limit) {
        Page<CardPrivilegeCode> cardPrivilegeCodePage = cardPrivilegeCodeQueryService.findAllWithPagination(page, limit);
        return cardPrivilegeCodeMapper.toDtoList(cardPrivilegeCodePage.getContent());
    }
}
