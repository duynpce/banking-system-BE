package com.example.banking_system.card.controller;

import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.card.service.domain.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cards")
public class CardController {
    private final CardService cardService;

    // Get all cards for the authenticated user by its username from JWT
    @GetMapping
    public ResponseEntity<List<? extends GetCardResponse>> getAllFromByJwt(){
        List<? extends GetCardResponse>response = cardService.GetAllCardByJwt();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCardResponse> getById(@PathVariable long id){
        GetCardResponse response = cardService.getCardById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GetCardResponse> delete(@PathVariable long id){
        cardService.deleteCardById( id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/cashback-rate/{id}")
//    public ResponseEntity<BigDecimal> getCashbackRate(@PathVariable long id) {
//        BigDecimal cashbackRate = cardService.getCashbackRateById(id);
//        return ResponseEntity.ok(cashbackRate);
//    }
}
