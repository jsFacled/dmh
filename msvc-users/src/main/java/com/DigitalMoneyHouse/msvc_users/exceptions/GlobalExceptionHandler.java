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

    // Maneja excepciones cuando un usuario ya existe.
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        // Retorna una respuesta con el código de estado 409 (Conflict) y un mensaje que indica que el usuario ya existe.
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("El usuario ya existe", "USER_ALREADY_EXISTS");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Maneja excepciones cuando la contraseña proporcionada es incorrecta.
    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorResponseDTO> handlePasswordIncorrectException(PasswordIncorrectException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        // Retorna una respuesta con el código de estado 400 (Bad Request) y un mensaje que indica que la contraseña es incorrecta.
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Contraseña incorrecta", "PASSWORD_INCORRECT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Maneja excepciones cuando un usuario no se encuentra.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("Error: " + ex.getMessage(), ex);
        // Retorna una respuesta con el código de estado 404 (Not Found) y un mensaje que indica que el usuario no se encuentra.
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Usuario inexistente", "USER_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Maneja excepciones generales no específicas.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Error no manejado: " + ex.getMessage(), ex);
        // Retorna una respuesta con el código de estado 500 (Internal Server Error) y un mensaje genérico para errores internos del servidor.
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Error interno del servidor", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}


/*
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

 */