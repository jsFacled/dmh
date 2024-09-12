package com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign;

import com.DigitalMoneyHouse.msvc_accounts.client.transactions.exceptions.TransactionErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class TransactionsFeignConfig {

    @Bean
    public ErrorDecoder transactionErrorDecoder() {
        return new TransactionErrorDecoder();
    }
}
