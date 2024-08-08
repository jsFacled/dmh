package com.DigitalMoneyHouse.msvc_cards.service;

import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.repository.ICardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Card> getCardsByAccountId(String accountId) {
        return cardRepository.findByAccountId(accountId);
    }
}