package com.example.banking_system.card.service.query;

import com.example.banking_system.card.entity.BusinessCard;
import com.example.banking_system.card.entity.PersonalCard;
import com.example.banking_system.card.repository.BusinessCardRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessCardQueryService {
    private final BusinessCardRepository businessCardRepository;

    public BusinessCard findById(Long id) {
        return businessCardRepository.findById(id).orElseThrow(
                () -> new NotFoundException("BusinessCard not found with id: " + id)
        );
    }

    public List<BusinessCard> getCardsFromAccountId(Long accountId) {
        return businessCardRepository.findByCard_Account_Id(accountId);
    }
}
