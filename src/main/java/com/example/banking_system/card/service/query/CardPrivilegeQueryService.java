package com.example.banking_system.card.service.query;

import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.repository.CardPrivilegeRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardPrivilegeQueryService {

    private final CardPrivilegeRepository cardPrivilegeRepository;

    public CardPrivilege save(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public CardPrivilege findByPrivilegeCodeAndIsActive(String privilegeCode) {
        return cardPrivilegeRepository.findByPrivilegeCodeAndDate(privilegeCode, LocalDate.now()).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode))
        );
    }

    public CardPrivilege findById(long id) {
        return cardPrivilegeRepository.findById(id).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with id: " + id))
        );
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
        CardPrivilege cardPrivilege = findById(id);
       cardPrivilegeRepository.deleteById(id);
    }

    public boolean hasOverlap(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.hasOverlap
                (cardPrivilege.getAccountType(), cardPrivilege.getCardType(), cardPrivilege.getEffectiveFrom(), cardPrivilege.getEffectiveTo());
    }

}
