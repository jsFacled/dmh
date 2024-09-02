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
        System.out.println(" -  -  -  ** -  -  - // -  - Desde ms-uaM manager/hello: dice Hola mundo!! /*  - - - - // ** // **.");
        return ResponseEntity.ok("Hola Mundo!! Soy ms-Manager!!!");
    }


@PostMapping("/register")
    public ResponseEntity<?> registerUserAccount(@RequestBody UserDTO userDTO) {
        try {
            return usersAccountsManagerService.registrarUserAccount(userDTO);
        } catch (ResponseStatusException e) {
            // Manejar la excepción y devolver una respuesta adecuada
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Manejar otras excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }









}
