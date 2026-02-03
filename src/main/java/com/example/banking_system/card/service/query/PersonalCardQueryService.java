package com.example.banking_system.card.service.query;

import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.repository.PersonalCardRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalCardQueryService {
    private final PersonalCardRepository personalCardRepository;

    public PersonalCard findById(Long id) {
        return personalCardRepository.findById(id).orElseThrow(
                () -> new NotFoundException("BusinessCard not found with id: " + id)
        );
    }

    public List<PersonalCard> getCardsFromAccountId(Long accountId) {
        return personalCardRepository.findByCard_Account_Id(accountId);
    }
}
