package com.example.banking_system.card.service.query;

import com.example.banking_system.card.entity.CardPrivilege;
import com.example.banking_system.card.repository.CardPrivilegeRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardPrivilegeQueryService {

    private final CardPrivilegeRepository cardPrivilegeRepository;

    public CardPrivilege save(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.save(cardPrivilege);
    }

    public CardPrivilege findByCode(String privilegeCode) {
        return cardPrivilegeRepository.findById(privilegeCode).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode))
        );
    }

    public void delete(CardPrivilege cardPrivilege) {
        cardPrivilegeRepository.delete(cardPrivilege);
    }

    public boolean isExistsAndActive(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.existsByAccountTypeAndCardTypeAndIsActiveTrue(cardPrivilege.getAccountType(), cardPrivilege.getCardType());
    }

}
