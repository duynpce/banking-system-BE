package com.example.banking_system.domain.card.controller;

import com.example.banking_system.common.dto.MetaDto;
import com.example.banking_system.domain.card.dto.GetCardResponse;
import com.example.banking_system.domain.card.service.domain.CardService;
import com.example.banking_system.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cards")
public class CardController {
    private final CardService cardService;

    // Get all cards for the authenticated user by its username from JWT
    @GetMapping(params = {"!page", "!limit"})
    // add meta data later
    public ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> getAllFromByJwt(){
        List<? extends GetCardResponse> response = cardService.GetAllCardByJwt();
        return ResponseEntity.ok(ResponseDto.success(response, "Cards retrieved successfully"));
    }

    @GetMapping(params = {"page", "limit"})
    public ResponseEntity<ResponseDto<List<? extends GetCardResponse>>> getFromByJwtWithPagination(
            @RequestParam int page,
            @RequestParam int limit
    ){
        List<? extends GetCardResponse> response = cardService.getCardsByJwtWithPagination(page, limit);
        MetaDto metaDto = cardService.getCardMetaDataByJwt(page, limit);
        return ResponseEntity.ok(ResponseDto.success(response, "Cards retrieved successfully", metaDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<GetCardResponse>> getById(@PathVariable long id){
        GetCardResponse response = cardService.getCardById(id);
        return ResponseEntity.ok(ResponseDto.success(response, "Card retrieved successfully"));
    }

    @GetMapping("/first")
    public ResponseEntity<ResponseDto<GetCardResponse>> getFirstByJwt(){
        GetCardResponse response = cardService.getFistCardByJwt();
        return ResponseEntity.ok(ResponseDto.success(response, "First card retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        cardService.deleteCardById( id);
        return ResponseEntity.noContent().build();
    }
}
