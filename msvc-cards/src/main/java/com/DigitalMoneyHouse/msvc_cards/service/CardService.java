package com.DigitalMoneyHouse.msvc_cards.service;

import com.DigitalMoneyHouse.msvc_cards.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_cards.exceptions.UnsupportedCardTypeException;
import com.DigitalMoneyHouse.msvc_cards.models.CardMapperGeneric;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardCreationDTO;
import com.DigitalMoneyHouse.msvc_cards.models.dto.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_cards.models.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.models.enums.CardType;
import com.DigitalMoneyHouse.msvc_cards.repository.ICardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final ICardRepository cardRepository;
    private final CardMapperGeneric cardMapper;

    public CardService(ICardRepository cardRepository, CardMapperGeneric cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    public List<CardRequestDTO> getAllCards() {
        // Obtener todas las entidades Card de la base de datos
        List<Card> cards = cardRepository.findAll();

        // Mapea cada entidad Card a su correspondiente CardRequestDTO
        return cards.stream()
                .map(cardMapper::toCardRequestDTO) // Utiliza el método mapeador
                .collect(Collectors.toList()); // Recoger los DTOs en una lista
    }

    // Método para obtener tarjetas por accountId
    public List<CardRequestDTO> getCardsByAccountId(Long accountId) {
        // Obtener las entidades Card asociadas a un accountId específico
        List<Card> cards = cardRepository.findByAccountId(accountId);

        // Mapear cada entidad Card a su correspondiente CardRequestDTO
        return cards.stream()
                .map(cardMapper::toCardRequestDTO) // Utiliza el método mapeador
                .collect(Collectors.toList()); // Recoger los DTOs en una lista
    }

    public List<Card> getCardsByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

    // Método para obtener una tarjeta por su ID
    public Optional<CardRequestDTO> getCardById(Long id) {
        // Obtener la entidad Card asociada al ID especificado
        Optional<Card> cardOptional = cardRepository.findById(id);

        // Mapear la entidad a CardRequestDTO si está presente
        return cardOptional.map(cardMapper::toCardRequestDTO);
    }




    @Transactional
    public void createCard(CardCreationDTO cardDTO) {
        // Verifica si la tarjeta ya existe
        boolean existingCard = cardRepository.existsByNumber(cardDTO.getNumber());
        if (existingCard) {
            // Si la tarjeta ya existe, lanza una excepción
            throw new CardAlreadyExistsException("La tarjeta ya está asociada a otra cuenta.");
        }

        Card newCard;
        if (cardDTO.getCardType() == CardType.CREDIT) {
            newCard = cardMapper.toCreditCard(cardDTO);
        } else if (cardDTO.getCardType() == CardType.DEBIT) {
            newCard = cardMapper.toDebitCard(cardDTO);
        } else {
            throw new UnsupportedCardTypeException("Tipo de tarjeta no soportado.");
        }

        cardRepository.save(newCard);
    }


    public boolean deleteCard(Long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        if (cardOptional.isPresent()) {
            cardRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Card> getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    public Boolean existsById(Long cardId) {
    return cardRepository.existsById(cardId);
    }

public String getNumberById(Long cardId){
        return cardRepository.getNumberById(cardId);
}

    @Transactional
    public void decreaseCreditLimit(Long cardId, BigDecimal amount) {
        int rowsAffected = cardRepository.decreaseCreditLimitIfSufficient(cardId, amount);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Límite de crédito insuficiente o tarjeta no encontrada");
        }
    }

    @Transactional
    public void decreaseDebitCardBalance(Long cardId, BigDecimal amount) {
        int rowsAffected = cardRepository.decreaseBalanceIfSufficient(cardId, amount);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente o tarjeta no encontrada");
        }
    }

    public boolean isCardAssociatedWithAccount(Long cardId, Long accountId) {
        return cardRepository.existsByIdAndAccountId(cardId, accountId);
    }

}

