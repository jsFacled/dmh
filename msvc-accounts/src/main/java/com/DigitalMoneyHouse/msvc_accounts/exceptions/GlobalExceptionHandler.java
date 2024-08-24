package com.DigitalMoneyHouse.msvc_accounts.exceptions;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.exceptions.CardAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Maneja las excepciones globales en msvc_accounts y también las relacionadas a client.cards y client.transactions.
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

    // Maneja excepciones cuando la tarjeta ya existe.
    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCardAlreadyExistsException(CardAlreadyExistsException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("La tarjeta ya está asociada a otra cuenta", "CARD_ALREADY_EXISTS");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    // Maneja excepciones específicas de deserialización JSON.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
// Mensaje de error generalizado para documentación
        String errorMessage = "Los datos proporcionados no son válidos. Asegúrese de que la solicitud siga la siguiente estructura:\n\n" +
                "{" +
                "\"expiration\": \"string\"," +
                "\"number\": \"string\"," +
                "\"holderName\": \"string\"," +
                "\"cvc\": \"string\"," +
                "\"cardType\": \"CREDIT o DEBIT\"," +
                "\"brand\": \"VISA, MASTERCARD, AMEX, UNKNOWN\"," +
                "\"creditLimit\": \"decimal (solo para CREDIT)\"," +
                "\"balance\": \"decimal (solo para DEBIT)\"" +
                "}\n\n" +
                "Revise los datos enviados y vuelva a intentarlo.";

        // Mensaje genérico para otros errores de deserialización
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorMessage, "INVALID_DATA_FORMAT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // Maneja excepciones generales.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Error no manejado: " + ex.getMessage(), ex);
        // Respuesta 500 (Internal Server Error) con un mensaje genérico.
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Error interno del servidor accounts", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}