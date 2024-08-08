package com.DigitalMoneyHouse.msvc_cards.dto;
import com.DigitalMoneyHouse.msvc_cards.enums.CardBrand;
import com.DigitalMoneyHouse.msvc_cards.enums.CardType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardDTO {

    private Long id;
    private String expiration;
    private String number;
    private String holderName;
    private String cvc;
    private Long accountId;
    private CardType cardType;
    private CardBrand brand;

}
