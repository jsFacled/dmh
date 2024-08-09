package com.DigitalMoneyHouse.msvc_cards.repository;
import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

    // Encontrar tarjetas por accountId
    List<Card> findByAccountId(Long accountId);

    // Encontrar tarjetas por userId
    List<Card> findByUserId(Long userId);
}
