package com.DigitalMoneyHouse.msvc_accounts.client.cards.cardsFeign;


import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CustomErrorDecoder;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "ms-cards", url = "http://localhost:8085")

public interface ICardFeignClient {

    @PostMapping("/cards")
    public ResponseEntity<?> createCard(@RequestBody CardCreationDTO cardDTO);

    @DeleteMapping("/cards/{id}")
    ResponseEntity<Void> deleteCard(@PathVariable("id") Long id);

    @GetMapping("/cards/account/{accountId}")
    ResponseEntity<List<CardRequestDTO>> getCardsByAccountId(@PathVariable("accountId") Long accountId);

    @GetMapping("cards/{cardId}")
    ResponseEntity<CardRequestDTO> getCardById(@PathVariable("cardId") Long cardId);

    @GetMapping("cards/{cardId}/exists")
    Boolean existsById (@PathVariable("cardId") Long cardId);


    @GetMapping("cards/{cardId}/number")
    String getNumberById (@PathVariable("cardId") Long cardId);

    // Método para disminuir el límite de crédito
    @PostMapping("/cards/decrease-limit")
    ResponseEntity<String> decreaseCreditLimit(
            @RequestParam("cardId") Long cardId,
            @RequestParam("amount") BigDecimal amount);

    // Método para disminuir el saldo de la tarjeta de débito
    @PostMapping("/cards/decrease-balance")
    ResponseEntity<String> decreaseDebitCardBalance(
            @RequestParam("cardId") Long cardId,
            @RequestParam("amount") BigDecimal amount);


    @GetMapping("/cards/{cardId}/associated-with-account/{accountId}")
    ResponseEntity<Boolean> isCardAssociatedWithAccount(@PathVariable("cardId") Long cardId, @PathVariable("accountId") Long accountId);


}
