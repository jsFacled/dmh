package com.DigitalMoneyHouse.msvc_accounts.client.cards;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}

