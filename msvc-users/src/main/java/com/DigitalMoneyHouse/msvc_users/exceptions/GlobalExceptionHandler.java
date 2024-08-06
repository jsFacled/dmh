package com.DigitalMoneyHouse.msvc_users.exceptions;
import com.DigitalMoneyHouse.msvc_users.dto.UserRegisteredResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * El controlador solo maneja la lógica de la solicitud y delega el manejo de excepciones al GlobalExceptionHandler.
 *
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<UserRegisteredResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserRegisteredResponseDTO(null, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserRegisteredResponseDTO> handleGenericException(Exception ex) {
        logger.error("Error no manejado: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserRegisteredResponseDTO(null, "Error interno del servidor"));
    }
}