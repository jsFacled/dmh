package com.DigitalMoneyHouse.msvc_accounts.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponseDTO {
    private String message;
    private String errorCode;

    public ErrorResponseDTO(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters y Setters

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}