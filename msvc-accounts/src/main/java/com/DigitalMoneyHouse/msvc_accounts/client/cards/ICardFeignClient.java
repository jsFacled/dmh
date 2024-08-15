package com.DigitalMoneyHouse.msvc_accounts.client.cards;


import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-cards", url = "http://localhost:8085")

public interface ICardFeignClient {

    @PostMapping("/cards")
    public ResponseEntity<Void> createCard(@RequestBody CardCreationDTO cardDTO);

    @DeleteMapping("/cards/{id}")
    ResponseEntity<Void> deleteCard(@PathVariable("id") Long id);

    @GetMapping("/cards/account/{accountId}")
    ResponseEntity<List<CardRequestDTO>> getCardsByAccountId(@PathVariable("accountId") Long accountId);

    @GetMapping("cards/{cardId}")
    ResponseEntity<CardRequestDTO> getCardById(@PathVariable("cardId") Long cardId);



}
