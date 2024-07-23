package com.DigitalMoneyHouse.msvc_usersAccountsManager.controller;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.service.UsersAccountsManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class UsersAccountsManagerController {

    private final UsersAccountsManagerService usersAccountsManagerService;

    public UsersAccountsManagerController(UsersAccountsManagerService usersAccountsManagerService) {
        this.usersAccountsManagerService = usersAccountsManagerService;
    }

    @PostMapping("/register")
        public ResponseEntity<?> registerUserAccount(@RequestBody UserDTO userDTO) {
            try {
                usersAccountsManagerService.registrarUserAccount(userDTO);
                return ResponseEntity.ok("Desde registerUserAccount Cuenta y usuario creados con éxito.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Desde registerUserAccount Error al crear usuario y cuenta: " + e.getMessage());
            }
        }


}
