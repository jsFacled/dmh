package com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CardErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Implementa el manejo de errores específico para tarjetas
        return new Exception("Card error for " + methodKey);
    }
}