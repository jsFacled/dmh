package com.DigitalMoneyHouse.msvc_accounts.client.cards;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/cards")
public class CardsController {

   private final CardsService cardsService;

    public CardsController(ICardFeignClient cardFeignClient, CardsService cardsService) {

        this.cardsService = cardsService;
    }
    @PostMapping
    public ResponseEntity<Void> crearTarjeta(@PathVariable Long accountId, @RequestBody CardRequestDTO cardRequest) {
            cardsService.createCard(accountId, cardRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();


    }
    @GetMapping
    public ResponseEntity<List<CardRequestDTO>> obtenerTarjetas(@PathVariable Long accountId) {
        List<CardRequestDTO> cards = cardsService.obtenerTarjetasPorCuenta(accountId);
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }
    @GetMapping("/{cardId}")
    public ResponseEntity<CardRequestDTO> obtenerTarjetaPorCardId(@PathVariable Long cardId) {
        try {
            // Llama al servicio para obtener la tarjeta por ID
            CardRequestDTO card = cardsService.obtenerTarjetaPorCardId(cardId);

            // Devuelve la tarjeta con un estado HTTP 200 (OK) si se encuentra
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            // Devuelve un estado HTTP 404 (Not Found) si la tarjeta no se encuentra
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> borrarTarjeta(@PathVariable Long accountId, @PathVariable Long cardId) {
        //cardService.borrarTarjeta(accountId, cardId);
       // return ResponseEntity.ok().build();
        return null;//implementar
    }
}
