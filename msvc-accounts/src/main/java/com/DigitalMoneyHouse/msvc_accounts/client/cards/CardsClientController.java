package com.DigitalMoneyHouse.msvc_accounts.client.cards;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_accounts.exceptions.AccountNotFoundException;
import com.DigitalMoneyHouse.msvc_accounts.exceptions.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accounts/{accountId}/cards")
public class CardsClientController {

   private final CardsClientService cardsService;

    public CardsClientController(CardsClientService cardsService) {

        this.cardsService = cardsService;
    }
    @PostMapping
    public ResponseEntity<?> crearTarjeta(@PathVariable Long accountId, @RequestBody CardRequestDTO cardRequest) {
        try {
            // Llama al servicio para crear la tarjeta
            ResponseEntity<?> serviceResponse = cardsService.createCard(accountId, cardRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(serviceResponse);
        } catch (AccountNotFoundException ex) {
            // Manejo específico para AccountNotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Cuenta no encontrada", ex.getMessage()));
        }catch (CardAlreadyExistsException ex) {
            // Manejo específico para CardAlreadyExistsException
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("La tarjeta ya existe", ex.getMessage()));
        }
        catch (Exception ex) {
            System.out.println("*/*/*/*/*/ Saltó excepción en cardsController: "+ex.getLocalizedMessage());
            System.out.println("*/*/*/*/*/ Excepción getCause: "+ex.getCause());
            // Manejo genérico para cualquier otra excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Error interno del servidor", ex.getMessage()));
        }
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
    public ResponseEntity<Void> borrarTarjeta(@PathVariable Long cardId) {

        return  cardsService.borrarTarjeta(cardId);

    }

}
