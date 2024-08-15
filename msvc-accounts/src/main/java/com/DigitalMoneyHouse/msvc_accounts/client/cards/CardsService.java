package com.DigitalMoneyHouse.msvc_accounts.client.cards;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardType;
import com.DigitalMoneyHouse.msvc_accounts.exceptions.AccountNotFoundException;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CardsService {

    private final ICardFeignClient cardFeignClient;
    private final IAccountRepository accountRepository;

    public CardsService(ICardFeignClient cardFeignClient, IAccountRepository accountRepository) {
        this.cardFeignClient = cardFeignClient;
        this.accountRepository = accountRepository;

    }

    //Recibe un RequestDTO, se evalúa para agregarle accountId y userId
    public void createCard(Long accountId, CardRequestDTO cardRequest) {
        // Obtener userId de la base de datos
        Long userId = accountRepository.findUserIdByAccountId(accountId);
        if (userId == null) {
            throw new AccountNotFoundException("El accountId proporcionado no corresponde a un usuario válido.");
        }
        // Crear el DTO completo

        CardCreationDTO cardCreationDTO = buildCardCreationDTO(accountId, cardRequest, userId);

        // Enviar el DTO al microservicio de tarjetas
        ResponseEntity<Void> response = cardFeignClient.createCard(cardCreationDTO);
        System.out.println("*/*/*/*/ cardCreationDTO es : "+cardCreationDTO);
        System.out.println("*/*/*/*/ el response que viene de ms-cards es : " + response);
        // Manejar la respuesta del microservicio
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new CardAlreadyExistsException("La tarjeta ya está asociada a otra cuenta.");
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException("Datos de la tarjeta no válidos.");
        }
    }

    //Método estático para crear un dto que será enviado a ms-cards
    private static CardCreationDTO buildCardCreationDTO(Long accountId, CardRequestDTO cardRequest, Long userId) {
        CardCreationDTO cardCreationDTO = new CardCreationDTO();

        cardCreationDTO.setUserId(userId);
        cardCreationDTO.setAccountId(accountId);
        cardCreationDTO.setExpiration(cardRequest.getExpiration());
        cardCreationDTO.setNumber(cardRequest.getNumber());
        cardCreationDTO.setHolderName(cardRequest.getHolderName());
        cardCreationDTO.setCvc(cardRequest.getCvc());
        cardCreationDTO.setCardType(cardRequest.getCardType());
        cardCreationDTO.setBrand(cardRequest.getBrand());

        // Establecer creditLimit o balance según el tipo de tarjeta
        if (cardRequest.getCardType() == CardType.CREDIT) {
            cardCreationDTO.setCreditLimit(cardRequest.getCreditLimit());
            cardCreationDTO.setBalance(null); // Asegurarse de que balance sea null para CreditCard
        } else if (cardRequest.getCardType() == CardType.DEBIT) {
            cardCreationDTO.setBalance(cardRequest.getBalance());
            cardCreationDTO.setCreditLimit(null); // Asegurarse de que creditLimit sea null para DebitCard
        }
        return cardCreationDTO;
    }

    // Método para obtener la lista de tarjetas por accountId
    public List<CardRequestDTO> obtenerTarjetasPorCuenta(Long accountId) {
        // Llama al cliente Feign para obtener las tarjetas asociadas a la cuenta
        ResponseEntity<List<CardRequestDTO>> response = cardFeignClient.getCardsByAccountId(accountId);

        // Verifica el estado de la respuesta
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Error al obtener las tarjetas para la cuenta con ID: " + accountId);
        }
    }

    // Método para obtener una tarjeta específica por su ID
    public CardRequestDTO obtenerTarjetaPorCardId(Long cardId) {
        // Llama al cliente Feign para obtener la tarjeta por su ID
        ResponseEntity<CardRequestDTO> response = cardFeignClient.getCardById(cardId);

        // Verifica el estado de la respuesta
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new RuntimeException("Tarjeta con ID: " + cardId + " no encontrada.");
        } else {
            throw new RuntimeException("Error al obtener la tarjeta con ID: " + cardId);
        }
    }
}
