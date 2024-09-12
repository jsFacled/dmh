package com.DigitalMoneyHouse.msvc_cards.exceptions;



public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(String message) {
        super(message);
    }
}
