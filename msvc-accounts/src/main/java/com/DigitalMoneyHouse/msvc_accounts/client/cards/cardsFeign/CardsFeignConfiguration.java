package com.DigitalMoneyHouse.msvc_accounts.client.cards.cardsFeign;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CardErrorDecoder;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class CardsFeignConfiguration {
    @Bean
    public ErrorDecoder cardErrorDecoder() {
        return new CardErrorDecoder();
    }
}

