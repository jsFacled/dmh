package com.DigitalMoneyHouse.msvc_transactions.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}