package com.DigitalMoneyHouse.msvc_cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja excepciones cuando la tarjeta ya existe.
    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCardAlreadyExistsException(CardAlreadyExistsException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("La tarjeta ya está asociada a otra cuenta", "CARD_ALREADY_EXISTS");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Maneja excepciones generales no específicas.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Error no manejado: " + ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Error interno del servidor", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}