package com.DigitalMoneyHouse.msvc_accounts.client.cards.models;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CardDTO {

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
