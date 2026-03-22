package com.example.banking_system.card.unit;

import com.example.banking_system.domain.account.entity.Account;
import com.example.banking_system.domain.account.service.query.AccountQueryService;
import com.example.banking_system.domain.card.dto.GetCardResponse;
import com.example.banking_system.domain.card.entity.Card;
import com.example.banking_system.domain.card.entity.PersonalCard;
import com.example.banking_system.domain.card.mapper.CardMapper;
import com.example.banking_system.domain.card.service.domain.CardService;
import com.example.banking_system.domain.card.service.query.CardQueryService;
import com.example.banking_system.common.UnitTest;
import com.example.banking_system.common.exception.ForbiddenException;
import com.example.banking_system.common.exception.NotFoundException;
import com.example.banking_system.common.utility.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CardServiceUnitTest extends UnitTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private CardQueryService cardQueryService;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    public void GetAllCardByJwtSuccess() {
        final String username = "username";
        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        PersonalCard personalCard = new PersonalCard();
        Card card = new Card();
        card.setId(1L);
        card.setAccount(account);
        personalCard.setCard(card);
        account.setCards(List.of(card));


        GetCardResponse response = new GetCardResponse();
        response.setId(1L);

        List<GetCardResponse> responseList = new ArrayList<>();
        responseList.add(response);

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(cardMapper.toDtoList(any())).thenReturn(responseList);

        List<? extends GetCardResponse> result = cardService.GetAllCardByJwt();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.getFirst().getId());
        verify(accountQueryService).findByUsername(username);
        verify(cardMapper).toDtoList(any());
    }

    @Test
    public void GetAllCardByJwtFailure_UserNotFound() {
        final String username = "nonexistent";

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenThrow(new NotFoundException("User not found with username: " + username));

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> cardService.GetAllCardByJwt());

        Assertions.assertEquals("User not found with username: " + username, exception.getMessage());
        verify(accountQueryService).findByUsername(username);
        verify(cardMapper, never()).toDtoList(any());
    }

    @Test
    public void getCardByIdSuccess() {
        final String username = "username";
        final long cardId = 1L;

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        PersonalCard personalCard = new PersonalCard();
        Card card = new Card();
        card.setId(cardId);
        card.setAccount(account);
        card.setCardDetails(personalCard);
        personalCard.setCard(card);

        GetCardResponse response = new GetCardResponse();
        response.setId(cardId);

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(cardQueryService.findById(cardId)).thenReturn(card);
        when(cardMapper.toDto(personalCard)).thenReturn(response);

        GetCardResponse result = cardService.getCardById(cardId);

        Assertions.assertEquals(cardId, result.getId());
        verify(accountQueryService).findByUsername(username);
        verify(cardQueryService).findById(cardId);
        verify(cardMapper).toDto(personalCard);
    }

    @Test
    public void getCardByIdFailure_ForbiddenAccess() {
        final String username = "username";
        final long cardId = 1L;

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        Account otherAccount = new Account();
        otherAccount.setId(2L);

        Card card = new Card();
        card.setId(cardId);
        card.setAccount(otherAccount);

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(cardQueryService.findById(cardId)).thenReturn(card);

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> cardService.getCardById(cardId));

        Assertions.assertEquals("You are not allowed to access this card", exception.getMessage());
        verify(accountQueryService).findByUsername(username);
        verify(cardQueryService).findById(cardId);
        verify(cardMapper, never()).toDto(any());
    }

    @Test
    public void deleteCardByIdSuccess() {
        final String username = "username";
        final long cardId = 1L;

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        Card card = new Card();
        card.setId(cardId);
        card.setAccount(account);

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(cardQueryService.findById(cardId)).thenReturn(card);
        doNothing().when(cardQueryService).delete(card);

        cardService.deleteCardById(cardId);

        verify(accountQueryService).findByUsername(username);
        verify(cardQueryService).findById(cardId);
        verify(cardQueryService).delete(card);
    }

    @Test
    public void deleteCardByIdFailure_ForbiddenAccess() {
        final String username = "username";
        final long cardId = 1L;

        Account account = new Account();
        account.setId(1L);
        account.setUsername(username);

        Account otherAccount = new Account();
        otherAccount.setId(2L);

        Card card = new Card();
        card.setId(cardId);
        card.setAccount(otherAccount);

        when(jwtUtil.getUsername()).thenReturn(username);
        when(accountQueryService.findByUsername(username)).thenReturn(account);
        when(cardQueryService.findById(cardId)).thenReturn(card);

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> cardService.deleteCardById(cardId));

        Assertions.assertEquals("You are not allowed to delete this card", exception.getMessage());
        verify(accountQueryService).findByUsername(username);
        verify(cardQueryService).findById(cardId);
        verify(cardQueryService, never()).delete(any());
    }
}
