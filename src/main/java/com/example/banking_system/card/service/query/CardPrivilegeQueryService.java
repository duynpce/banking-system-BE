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

    public CardPrivilege findByPrivilegeCode(String privilegeCode) {
        return cardPrivilegeRepository.findByCardPrivilegeCode_CodeAndIsActiveTrue(privilegeCode).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode))
        );
    }

    public void delete(CardPrivilege cardPrivilege) {
        cardPrivilegeRepository.delete(cardPrivilege);
    }

    //temporary for test
    public void deleteByPrivilegeCode(String privilegeCode) {
        CardPrivilege cardPrivilege = findByPrivilegeCode(privilegeCode);
        delete(cardPrivilege);
    }

    public boolean isExistsAndActive(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.existsByAccountTypeAndCardTypeAndIsActiveTrue(cardPrivilege.getAccountType(), cardPrivilege.getCardType());
    }

}
