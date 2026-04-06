package com.example.banking_system.domain.card.service.query;

import com.example.banking_system.domain.card.entity.CardPrivilege;
import com.example.banking_system.domain.card.repository.CardPrivilegeRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardPrivilegeQueryService {

    private final CardPrivilegeRepository cardPrivilegeRepository;

    public CardPrivilege save(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public CardPrivilege findByPrivilegeCodeAndIsActive(String privilegeCode) {
        return cardPrivilegeRepository.findByPrivilegeCodeAndDate(privilegeCode, LocalDate.now(ZoneOffset.UTC)).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode))
        );
    }

    public CardPrivilege findById(long id) {
        return cardPrivilegeRepository.findById(id).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with id: " + id))
        );
    }

    public List<CardPrivilege> findAll() {
        return cardPrivilegeRepository.findAll();
    }

    public Page<CardPrivilege> findAllWithPagination(int page, int limit) {
        return cardPrivilegeRepository.findAll(PageRequest.of(page, limit));
    }

    public void delete(CardPrivilege cardPrivilege) {
        cardPrivilegeRepository.delete(cardPrivilege);
    }

    //temporary for test
    public void deleteByPrivilegeCode(String privilegeCode) {
        CardPrivilege cardPrivilege = findByPrivilegeCodeAndIsActive(privilegeCode);
        delete(cardPrivilege);
    }

    public void deleteById(long id) {
        findById(id);
       cardPrivilegeRepository.deleteById(id);
    }

    public boolean hasOverlap(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.hasOverlap
                (cardPrivilege.getAccountType(), cardPrivilege.getCardType(), cardPrivilege.getEffectiveFrom(), cardPrivilege.getEffectiveTo());
    }

    public boolean hasCodeOverlap(String code, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return cardPrivilegeRepository.hasCodeOverlap(code, effectiveFrom, effectiveTo);
    }

    public boolean hasCodeOverlapExcludingId(String code, LocalDate effectiveFrom, LocalDate effectiveTo, Long excludeId) {
        return cardPrivilegeRepository.hasCodeOverlapExcludingId(code, effectiveFrom, effectiveTo, excludeId);
    }

    public boolean existsByCode(String code) {
        return cardPrivilegeRepository.findByPrivilegeCodeAndDate(code, LocalDate.now(ZoneOffset.UTC)).isPresent();
    }

}
