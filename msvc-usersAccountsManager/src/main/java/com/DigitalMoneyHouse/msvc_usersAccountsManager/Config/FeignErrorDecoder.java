package com.DigitalMoneyHouse.msvc_usersAccountsManager.Config;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.exceptions.UserAlreadyExistsException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.CONFLICT.value()) {
            return new UserAlreadyExistsException("El usuario ya está registrado.");
        }
        return new Exception("Error desconocido");
    }
}