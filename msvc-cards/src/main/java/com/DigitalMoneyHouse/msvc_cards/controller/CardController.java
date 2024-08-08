package com.DigitalMoneyHouse.msvc_cards.controller;


import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    // Endpoint para obtener tarjetas por accountId
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Card>> getCardsByAccountId(@PathVariable String accountId) {
        List<Card> cards = cardService.getCardsByAccountId(accountId);
        return ResponseEntity.ok(cards);
    }
}