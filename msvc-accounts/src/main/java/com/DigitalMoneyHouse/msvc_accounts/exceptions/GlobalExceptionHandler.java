package com.DigitalMoneyHouse.msvc_accounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;

/**
 * Maneja las excepciones globales en msvc_accounts.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja excepciones cuando una entidad no se encuentra (como Account).
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        // Respuesta 404 (Not Found) con un mensaje adecuado
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("No se encontró la cuenta solicitada", "ACCOUNT_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Maneja excepciones generales.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Error no manejado: " + ex.getMessage(), ex);
        // Respuesta 500 (Internal Server Error) con un mensaje genérico.
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Error interno del servidor", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}