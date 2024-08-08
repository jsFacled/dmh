package com.DigitalMoneyHouse.msvc_cards.repository;
import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {
    // Puedes agregar métodos personalizados si es necesario

    // Método personalizado para encontrar tarjetas por accountId
    List<Card> findByAccountId(String accountId);
}
