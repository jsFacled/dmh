package com.DigitalMoneyHouse.msvc_accounts.client.transactions.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class TransactionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Implementa el manejo de errores específico para transacciones
        return new Exception("Transaction error for " + methodKey);
    }
}