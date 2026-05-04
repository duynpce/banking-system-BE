package com.example.banking_system.domain.card.service.query;

import com.example.banking_system.domain.account.constant.AccountType;
import com.example.banking_system.domain.card.constant.CardType;
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

    public CardPrivilege findByCodeAndAccountTypeAndCardTypeAndIsActive(String privilegeCode, AccountType accountType, CardType cardType) {
        return cardPrivilegeRepository.findByCodeAndAccountTypeAndCardTypeAndDate(privilegeCode, accountType, cardType, LocalDate.now(ZoneOffset.UTC)).orElseThrow(
                () -> new NotFoundException(("no active Card privilege found with code: " + privilegeCode + " and account type: " + accountType + " and card type: " + cardType))
        );
    }

    public CardPrivilege findByCodeAndAccountTypeAndCardTypeAndDate(String privilegeCode, AccountType accountType, CardType cardType, LocalDate date) {
        return cardPrivilegeRepository.findByCodeAndAccountTypeAndCardTypeAndDate(privilegeCode, accountType, cardType, date).orElseThrow(
                () -> new NotFoundException(("Card privilege not found with code: " + privilegeCode + " and account type: " + accountType + " and card type: " + cardType + " and date: " + date))
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

    public List<CardPrivilege> findByAccountTypeAndCardTypeAndIsActive(AccountType accountType, CardType cardType) {
        return cardPrivilegeRepository.findByAccountTypeAndCardTypeAndDate(accountType, cardType, LocalDate.now(ZoneOffset.UTC));
    }

    public void delete(CardPrivilege cardPrivilege) {
        cardPrivilegeRepository.delete(cardPrivilege);
    }

    public void deleteByPrivilegeCodeAndAccountTypeAndCardType(String privilegeCode, AccountType accountType, CardType cardType) {
        CardPrivilege cardPrivilege = findByCodeAndAccountTypeAndCardTypeAndIsActive(privilegeCode, accountType, cardType);
        delete(cardPrivilege);
    }

    public void deleteById(long id) {
        findById(id);
       cardPrivilegeRepository.deleteById(id);
    }

    public boolean hasOverlap(CardPrivilege cardPrivilege) {
        return cardPrivilegeRepository.hasOverlap
                (cardPrivilege.getCode(),
                        cardPrivilege.getAccountType(),
                        cardPrivilege.getCardType(),
                        cardPrivilege.getEffectiveFrom(),
                        cardPrivilege.getEffectiveTo()
                );
    }
}
