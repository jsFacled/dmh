package com.DigitalMoneyHouse.msvc_cards.service;

import com.DigitalMoneyHouse.msvc_cards.dto.CardDTO;
import com.DigitalMoneyHouse.msvc_cards.dto.ICardMapper;
import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.enums.CardType;
import com.DigitalMoneyHouse.msvc_cards.repository.ICardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {


    private final ICardRepository cardRepository;
    private final ICardMapper cardMapper;

    public CardService(ICardRepository cardRepository, ICardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
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


    @Transactional
    public void createCard(CardDTO cardDTO) {
        Card newCard;

        if (cardDTO.getCardType() == CardType.CREDIT) {
            newCard = cardMapper.toCreditCard(cardDTO);
        } else if (cardDTO.getCardType() == CardType.DEBIT) {
            newCard = cardMapper.toDebitCard(cardDTO);
        } else {
            throw new IllegalArgumentException("Tipo de tarjeta no soportado.");
        }

        cardRepository.save(newCard);
    }

    /*
    // Método para crear una nueva tarjeta sin Mapstruct
    public void createCard(CardDTO cardDTO) {
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


        cardRepository.save(newCard);
    }
*/
    public boolean deleteCard(Long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        if (cardOptional.isPresent()) {
            cardRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}