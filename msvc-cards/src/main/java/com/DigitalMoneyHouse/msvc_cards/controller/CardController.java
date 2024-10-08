package com.DigitalMoneyHouse.msvc_cards.controller;


import com.DigitalMoneyHouse.msvc_cards.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_cards.exceptions.ErrorResponseDTO;
import com.DigitalMoneyHouse.msvc_cards.exceptions.UnsupportedCardTypeException;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_cards.models.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

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

    @GetMapping("/by-number/{number}")
    public ResponseEntity<?> getCardByNumber(@PathVariable String number) {
        Optional<Card> card = cardService.getCardByNumber(number);

        if (card.isPresent()) {
            return ResponseEntity.ok(card.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "No se encuentra card con ese número");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/{cardId}/exists")
    Boolean existsById (@PathVariable("cardId") Long cardId){
     return cardService.existsById(cardId);
    }

    @GetMapping("/{cardId}/number")
    String getNumberById (@PathVariable("cardId") Long cardId){
        return cardService.getNumberById(cardId);
    }


    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CardCreationDTO cardDTO) {
        try {
           cardService.createCard(cardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(cardDTO);
        } catch (CardAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDTO(e.getMessage(), "CARD_ALREADY_EXISTS"));
        } catch (UnsupportedCardTypeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(e.getMessage(), "UNSUPPORTED_CARD_TYPE"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Error interno del servidor", "INTERNAL_SERVER_ERROR"));
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

    @PostMapping("decrease-limit")
    public ResponseEntity<String> decreaseCreditLimit(
            @RequestParam Long cardId,
            @RequestParam BigDecimal amount) {
        try {
            cardService.decreaseCreditLimit(cardId, amount);
            return ResponseEntity.ok("Límite de crédito disminuido exitosamente");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al disminuir el límite de crédito");
        }
    }

    @PostMapping("decrease-balance")
    public ResponseEntity<String> decreaseDebitCardBalance(
            @RequestParam Long cardId,
            @RequestParam BigDecimal amount) {
        try {
            cardService.decreaseDebitCardBalance(cardId, amount);
            return ResponseEntity.ok("Saldo de la tarjeta de débito disminuido exitosamente");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al disminuir el saldo de la tarjeta de débito");
        }
    }

    @GetMapping("/{cardId}/associated-with-account/{accountId}")
    public ResponseEntity<Boolean> isCardAssociatedWithAccount(@PathVariable Long cardId, @PathVariable Long accountId) {
        boolean isAssociated = cardService.isCardAssociatedWithAccount(cardId, accountId);
        return ResponseEntity.ok(isAssociated);
    }

}