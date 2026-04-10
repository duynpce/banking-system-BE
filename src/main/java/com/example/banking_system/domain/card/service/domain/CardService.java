package com.example.banking_system.domain.card.service.domain;

import com.example.banking_system.common.dto.MetaDto;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.card.dto.GetCardResponse;
import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.card.entity.Card;
import com.example.banking_system.domain.card.entity.CardDetails;
import com.example.banking_system.domain.card.mapper.CardMapper;
import com.example.banking_system.domain.card.service.query.CardPrivilegeQueryService;
import com.example.banking_system.domain.card.service.query.CardQueryService;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final AccountQueryService accountQueryService;
    private final CardPrivilegeQueryService cardPrivilegeQueryService;
//    private final PricingMultiplierService pricingMultiplierService;
    private final JwtUtil jwtUtil;
    private final CardQueryService cardQueryService;
    private final CardMapper cardMapper;

    @Value("${value.bin}")
    private String BIN;

    public void updateExpirationDateOnCreate(Card card) {

        int baseExpirationYears = cardPrivilegeQueryService.findByCodeAndAccountTypeAndCardTypeAndIsActive(
                card.getPrivilege().getPrivilegeCode(),
                card.getPrivilege().getAccountType(),
                card.getType()
        ).getExpirationYears();
        LocalDate expirationDate = LocalDate.now().plusYears(baseExpirationYears);
        card.setExpirationDate(expirationDate);

    }

    //later use
    public void extendExpirationDate(Card card, int yearsToExtend) {
        card.setExpirationDate(card.getExpirationDate().plusYears(yearsToExtend));
    }

    @Transactional(readOnly = true)
    public List<? extends GetCardResponse> GetAllCardByJwt() {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        List<CardDetails> cardDetailsList = account.getCardDetailsList();
        return cardMapper.toDtoList(cardDetailsList);
    }

    @Transactional(readOnly = true)
    public List<? extends GetCardResponse> getCardsByJwtWithPagination(int page, int limit) {
        final String username = jwtUtil.getUsername();
        Page<Card> cardPage = cardQueryService.findByUsernameWithPagination(username, page, limit);

        List<CardDetails> cardDetailsList = cardPage.getContent().stream()
                .map(Card::getCardDetails)
                .toList();

        return cardMapper.toDtoList(cardDetailsList);
    }

    @Transactional(readOnly = true)
    public GetCardResponse getFistCardByJwt() {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);

        if(account.getCardDetailsList().isEmpty()) {
            return null;
        }

        CardDetails firstCard = account.getCardDetailsList().getFirst();
        return  cardMapper.toDto(firstCard);
    }

    @Transactional(readOnly = true)
    public GetCardResponse getCardById(long id) {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        Card card = cardQueryService.findById(id);

        if(card.getAccount().getId() != account.getId()) {
            throw new ForbiddenException("You are not allowed to access this card");
        }


        return cardMapper.toDto(card.getCardDetails());
    }


    public void deleteCardById(long id) {
        final String username = jwtUtil.getUsername();
        Account account = accountQueryService.findByUsername(username);
        Card card = cardQueryService.findById(id);

        if(card.getAccount().getId() != account.getId()) {
            throw new ForbiddenException("You are not allowed to delete this card");
        }

        cardQueryService.delete(card);
    }


    public String generateCardNumber() {
        String sequence = String.valueOf(cardQueryService.getCardNumberSequence()).formatted("%012d");
        return BIN + sequence;
    }

    public MetaDto getCardMetaDataByJwt(int currentPage, int limit) {
        final String username = jwtUtil.getUsername();
        int totalCards = cardQueryService.countByUsername(username);
        int totalPages = (int) Math.ceil((double) totalCards / limit);

        return MetaDto.builder().totalItems(totalCards)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .pageSize(limit)
                .build();

    }
}
