package com.DigitalMoneyHouse.msvc_cards.exceptions;

public class CardNotFoundException extends RuntimeException{

    // Constructor que acepta un mensaje de error.
    public CardNotFoundException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje de error y una causa.
    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
