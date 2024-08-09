package com.DigitalMoneyHouse.msvc_cards.dto;
import com.DigitalMoneyHouse.msvc_cards.enums.CardBrand;
import com.DigitalMoneyHouse.msvc_cards.enums.CardType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CardDTO {

    private Long id;
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
