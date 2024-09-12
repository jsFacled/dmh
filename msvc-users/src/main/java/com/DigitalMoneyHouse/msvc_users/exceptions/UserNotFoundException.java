package com.DigitalMoneyHouse.msvc_users.exceptions;



public class UserNotFoundException extends RuntimeException {

    // Constructor que acepta un mensaje de error.
    public UserNotFoundException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje de error y una causa.
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
