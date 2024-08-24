package com.DigitalMoneyHouse.msvc_accounts.client.transactions.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TransactionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String errorMessage = "Error desconocido";

        // Extrae el cuerpo de la respuesta si está disponible
        try (InputStream body = response.body().asInputStream()) {
            errorMessage = new BufferedReader(new InputStreamReader(body)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            // Manejo de errores en la lectura del cuerpo
            errorMessage = "No se pudo leer el cuerpo del error.";
        }

        switch (status) {
            case NOT_FOUND:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
            case BAD_REQUEST:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
            default:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }
}
