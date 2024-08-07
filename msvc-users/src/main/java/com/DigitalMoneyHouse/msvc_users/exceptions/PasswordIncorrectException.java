package com.DigitalMoneyHouse.msvc_users.exceptions;


public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException(String message) {
        super(message);
    }
}
