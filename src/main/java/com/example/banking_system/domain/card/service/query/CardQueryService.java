package com.example.banking_system.domain.card.service.query;

import com.example.banking_system.domain.card.entity.Card;
import com.example.banking_system.domain.card.repository.CardRepository;
import com.example.banking_system.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardQueryService {
    private final CardRepository cardRepository;

    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Card not found with id: " + id)
        );
    }

    public Page<Card> findByUsernameWithPagination(String username, int page, int limit) {
        return cardRepository.findByAccount_Username(username, PageRequest.of(page, limit));
    }

    public void delete(Card card) {
        cardRepository.delete(card);
    }

    public long getCardNumberSequence() {
        return cardRepository.getCardNumberSequence();
    }
}
