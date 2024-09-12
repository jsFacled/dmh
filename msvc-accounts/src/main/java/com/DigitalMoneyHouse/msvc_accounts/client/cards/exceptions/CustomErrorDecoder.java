package com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

/**
 * Lo utilizo para manejar errores que provienen de otros microservicios.
 * Librería de openFeign.
 */

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.CONFLICT.value()) {
            return new CardAlreadyExistsException("La tarjeta ya está asociada a otra cuenta.");
        }

        // Para otros errores, deja que el decodificador predeterminado maneje la excepción
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
