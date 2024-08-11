package com.DigitalMoneyHouse.msvc_cards.dto;

import com.DigitalMoneyHouse.msvc_cards.enums.CardBrand;
import com.DigitalMoneyHouse.msvc_cards.enums.CardType;

import java.math.BigDecimal;

public class CreditCardDTO {


    private String expiration;
    private String number;
    private String holderName;
    private String cvc;
    private Long userId;
    private Long accountId;
    private CardType cardType;
    private CardBrand brand;
    private BigDecimal creditLimit;// Solo para CreditCard
    private BigDecimal balance;// Solo para DebitCard
}
