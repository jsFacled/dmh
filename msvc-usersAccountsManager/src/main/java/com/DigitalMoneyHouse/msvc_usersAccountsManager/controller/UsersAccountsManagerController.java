package com.DigitalMoneyHouse.msvc_usersAccountsManager.controller;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.service.UsersAccountsManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/manager")
public class UsersAccountsManagerController {
    private static final Logger logger = LoggerFactory.getLogger(UsersAccountsManagerController.class);

    private final UsersAccountsManagerService usersAccountsManagerService;

    public UsersAccountsManagerController(UsersAccountsManagerService usersAccountsManagerService) {
        this.usersAccountsManagerService = usersAccountsManagerService;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> index()    {
        logger.info("Endpoint /manager/hello fue llamado");
        System.out.println(" -  -  -  ** -  -  - // -  - Desde ms-uaM manager/hello: dice Hola mundo!! /*  - - - - // ** // **.");
        return ResponseEntity.ok("Hola Mundo!! Soy ms-Manager!!!");
    }


@PostMapping("/register")
    public ResponseEntity<?> registerUserAccount(@RequestBody UserDTO userDTO) {
    logger.info("Inicio del registro de usuario: {}", userDTO);
        try {
            ResponseEntity<?> response = usersAccountsManagerService.registrarUserAccount(userDTO);
            logger.info("Registro de usuario exitoso para el usuario: {}", userDTO.getEmail());
            return response;
        } catch (ResponseStatusException e) {
            logger.error("Error en la solicitud: {}", e.getReason(), e);
            // Manejar la excepción y devolver una respuesta adecuada
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            logger.error("Error inesperado durante el registro de usuario: {}", userDTO.getEmail(), e);
            // Manejar otras excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/validate/{accountId}/{userId}")
    public ResponseEntity<Boolean> validateUserForAccount(@PathVariable Long accountId, @PathVariable Long userId) {
        boolean hasAccess = usersAccountsManagerService.isAuthorizedForAccount(accountId, userId);
        return ResponseEntity.ok(hasAccess);
    }










}
