package com.DigitalMoneyHouse.msvc_accounts.client.cards;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-cards", url = "${ms-cards.url}")

public interface ICardFeignClient {

    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO);

    @DeleteMapping("/cards/{id}")
    ResponseEntity<Void> deleteCard(@PathVariable("id") Long id);

    @GetMapping("/cards/account/{accountId}")
    ResponseEntity<List<CardDTO>> getCardsByAccountId(@PathVariable("accountId") Long accountId);



}
