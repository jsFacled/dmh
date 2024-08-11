package com.DigitalMoneyHouse.msvc_accounts.client.cards;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/cards")
public class CardsController {

    private final ICardFeignClient cardFeignClient;

    public CardsController(ICardFeignClient cardFeignClient) {
        this.cardFeignClient = cardFeignClient;
    }

    @PostMapping
    public ResponseEntity<CardDTO> crearTarjeta(@PathVariable Long accountId, @RequestBody CardDTO cardRequest) {
        CardDTO card = cardFeignClient.createCard(accountId, cardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> obtenerTarjetas(@PathVariable Long accountId) {
        List<CardDTO> cards = cardService.obtenerTarjetasPorCuenta(accountId);
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDTO> obtenerTarjeta(@PathVariable Long accountId, @PathVariable Long cardId) {
        Card card = cardService.obtenerTarjetaPorId(accountId, cardId);
        return card != null ? ResponseEntity.ok(card) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> borrarTarjeta(@PathVariable Long accountId, @PathVariable Long cardId) {
        cardService.borrarTarjeta(accountId, cardId);
        return ResponseEntity.ok().build();
    }
}
