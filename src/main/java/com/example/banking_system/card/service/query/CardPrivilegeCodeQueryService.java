package com.example.banking_system.card.service.query;

import com.example.banking_system.card.entity.CardPrivilegeCode;
import com.example.banking_system.card.repository.CardPrivilegeCodeRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardPrivilegeCodeQueryService {

    private final CardPrivilegeCodeRepository cardPrivilegeCodeRepository;

    public CardPrivilegeCode save(CardPrivilegeCode cardPrivilegeCode) {
        return cardPrivilegeCodeRepository.save(cardPrivilegeCode);
    }

    public CardPrivilegeCode findByCode(String code) {
        return cardPrivilegeCodeRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Card privilege code not found with code: " + code));
    }

    public void delete(CardPrivilegeCode cardPrivilegeCode) {
        cardPrivilegeCodeRepository.delete(cardPrivilegeCode);
    }

    public void deleteByCode(String code) {
        CardPrivilegeCode cardPrivilegeCode = findByCode(code);
        delete(cardPrivilegeCode);
    }

    public boolean existsByCode(String code) {
        return cardPrivilegeCodeRepository.existsById(code);
    }
}
