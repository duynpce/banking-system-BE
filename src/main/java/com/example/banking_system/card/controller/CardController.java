package com.example.banking_system.card.controller;

import com.example.banking_system.card.dto.GetCardResponse;
import com.example.banking_system.card.service.domain.CardService;
import com.example.banking_system.common.dto.ResponseDto;
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
    // add meta data later above it
    public ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> getAllFromByJwt(){
        List<? extends GetCardResponse> response = cardService.GetAllCardByJwt();
        return ResponseEntity.ok(ResponseDto.success(response, "Cards retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetCardResponse>> getById(@PathVariable long id){
        GetCardResponse response = cardService.getCardById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Card retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        cardService.deleteCardById( id);
        return ResponseEntity.noContent().build();
    }

    //later reimplement
//    @GetMapping("/cashback-rate/{id}")
//    public ResponseEntity<BigDecimal> getCashbackRate(@PathVariable long id) {
//        BigDecimal cashbackRate = cardService.getCashbackRateById(id);
//        return ResponseEntity.ok(cashbackRate);
//    }
}
