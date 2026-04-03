package com.example.banking_system.domain.card.service.query;

import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.domain.card.entity.CardPrivilegeCode;
import com.example.banking_system.domain.card.repository.CardPrivilegeCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CardPrivilegeCodeQueryService {

    private final CardPrivilegeCodeRepository cardPrivilegeCodeRepository;

    public CardPrivilegeCode save(CardPrivilegeCode cardPrivilegeCode) {
        return cardPrivilegeCodeRepository.save(cardPrivilegeCode);
    }

    public CardPrivilegeCode findByCodeAndIsActive(String code) {
        return cardPrivilegeCodeRepository.findByCodeAndDate(code, LocalDate.now(ZoneOffset.UTC)).orElseThrow(
                () -> new NotFoundException(("no active code with code: " + code))
        );
    }

    public CardPrivilegeCode findByCodeAndDate(String code, LocalDate date) {
        return cardPrivilegeCodeRepository.findByCodeAndDate(code, date).orElseThrow(
                () -> new NotFoundException(("no code with date : " + code + " and date: " + date))
        );
    }

    public CardPrivilegeCode findById(long id) {
        return cardPrivilegeCodeRepository.findById(id).orElseThrow(
                () -> new NotFoundException(("Card privilege code not found with id: " + id))
        );
    }

    //temporary for test will be replaced by api delete by id later
    public void deleteByCodeAndIsActive(String code) {
        CardPrivilegeCode cardPrivilegeCode = findByCodeAndIsActive(code);
        cardPrivilegeCodeRepository.delete(cardPrivilegeCode);
    }

    public void deleteById(long id) {
        CardPrivilegeCode cardPrivilegeCode = findById(id);
        cardPrivilegeCodeRepository.delete(cardPrivilegeCode);
    }

    public boolean existsById(long id) {
        return cardPrivilegeCodeRepository.existsById(id);
    }

    //temp for test, will be replaced by check for overlapping date range later
    public boolean existsByCode(String code) {
        return cardPrivilegeCodeRepository.existsByCode(code);
    }

    public boolean hasOverlap(String code, LocalDate effectiveFrom, LocalDate effectiveTo) {
        return cardPrivilegeCodeRepository.hasOverlap(code, effectiveFrom, effectiveTo);
    }

    public boolean hasOverlapExcludingId(String code, LocalDate effectiveFrom, LocalDate effectiveTo, Long excludeId) {
        return cardPrivilegeCodeRepository.hasOverlapExcludingId(code, effectiveFrom, effectiveTo, excludeId);
    }
}
