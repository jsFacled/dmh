package com.DigitalMoneyHouse.msvc_cards.entity;

import com.DigitalMoneyHouse.msvc_cards.enums.CardBrand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
public class DebitCard extends Card {
    // Getters y Setters
    @Getter
    private BigDecimal balance; // Saldo disponible

    @Enumerated(EnumType.STRING)
    @Column(name = "brand", nullable = false)
    private CardBrand brand;

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}