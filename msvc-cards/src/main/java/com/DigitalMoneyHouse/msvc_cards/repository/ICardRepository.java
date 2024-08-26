package com.DigitalMoneyHouse.msvc_cards.repository;
import com.DigitalMoneyHouse.msvc_cards.models.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

    // Encontrar tarjetas por accountId
    List<Card> findByAccountId(Long accountId);

    // Encontrar tarjetas por userId
    List<Card> findByUserId(Long userId);

    @Query("SELECT c FROM Card c WHERE c.number = :number")
    Optional<Card> findByNumber(@Param("number") String number);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Card c WHERE c.number = :number")
    boolean existsByNumber(@Param("number") String number);

    @Query("SELECT c.number FROM Card c WHERE c.id = :id")
    String getNumberById(@Param("id") Long id);
}
