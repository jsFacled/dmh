package com.DigitalMoneyHouse.msvc_cards.controller;


import com.DigitalMoneyHouse.msvc_cards.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_cards.models.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardRequestDTO>> getAllCards() {
        List<CardRequestDTO> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    // Endpoint para obtener tarjetas por accountId
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardRequestDTO>> getCardsByAccountId(@PathVariable Long accountId) {
        List<CardRequestDTO> cards = cardService.getCardsByAccountId(accountId);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Card>> getCardsByUserId(@PathVariable Long userId) {
        List<Card> cards = cardService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardRequestDTO> getCardById(@PathVariable Long id) {
        return cardService.getCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Void> createCard(@RequestBody CardCreationDTO cardDTO) {
        try {
            cardService.createCard(cardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (CardAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            // Imprime el stack trace para depuración
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        boolean isDeleted = cardService.deleteCard(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}