package com.DigitalMoneyHouse.msvc_cards.service;

import com.DigitalMoneyHouse.msvc_cards.dto.CardDTO;
import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.entity.CreditCard;
import com.DigitalMoneyHouse.msvc_cards.entity.DebitCard;
import com.DigitalMoneyHouse.msvc_cards.enums.CardType;
import com.DigitalMoneyHouse.msvc_cards.repository.ICardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {


    private final ICardRepository cardRepository;

    public CardService(ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
    // Método para obtener tarjetas por accountId
    public List<Card> getCardsByAccountId(Long accountId) {
        return cardRepository.findByAccountId(accountId);
    }

    public List<Card> getCardsByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    // Método para crear una nueva tarjeta
    public Card createCard(CardDTO cardDTO) {
        Card newCard;

        if (cardDTO.getCardType() == CardType.CREDIT) {
            CreditCard creditCard = new CreditCard();
            creditCard.setCreditLimit(cardDTO.getCreditLimit()); // O puedes recibirlo desde el DTO si es necesario
            creditCard.setBrand(cardDTO.getBrand());
            newCard = creditCard;
        } else if (cardDTO.getCardType() == CardType.DEBIT) {
            DebitCard debitCard = new DebitCard();
            debitCard.setBalance(BigDecimal.ZERO); // O puedes recibirlo desde el DTO si es necesario
            debitCard.setBrand(cardDTO.getBrand());
            newCard = debitCard;
        } else {
            throw new IllegalArgumentException("Tipo de tarjeta no soportado.");
        }

        // Setear los campos comunes desde el DTO
        newCard.setExpiration(cardDTO.getExpiration());
        newCard.setNumber(cardDTO.getNumber());
        newCard.setHolderName(cardDTO.getHolderName());
        newCard.setCvc(cardDTO.getCvc());
        newCard.setAccountId(cardDTO.getAccountId());
        newCard.setUserId(cardDTO.getUserId());

        return cardRepository.save(newCard);
    }
}