package com.example.banking_system.controller.card;

import com.example.banking_system.dto.card.GetCardResponse;
import com.example.banking_system.service.account.AccountService;
import com.example.banking_system.service.card.CardService;
import com.example.banking_system.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cards")
public class CardController {
    private final CardService cardService;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    // Get all cards for the authenticated user by its username from JWT
    @GetMapping
    public ResponseEntity<List<? extends GetCardResponse>> getAll(){
        final String username = jwtUtil.getUsername();
        List<? extends GetCardResponse>response = cardService.GetAllByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<GetCardResponse> getById(@PathVariable long cardId){
        final String username = jwtUtil.getUsername();
        GetCardResponse response = cardService.getCardByUsernameAndCardId(username, cardId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<GetCardResponse> delete(@RequestParam long cardId){
        final String username = jwtUtil.getUsername();
        cardService.deleteCardByUsernameAndCardId(username, cardId);
        return ResponseEntity.noContent().build();
    }

}
