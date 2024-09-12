package com.DigitalMoneyHouse.msvc_usersAccountsManager.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}