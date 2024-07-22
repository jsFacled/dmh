package com.DigitalMoneyHouse.msvc_accounts.controller;

import com.DigitalMoneyHouse.msvc_accounts.dto.AccountResponseDTO;
import com.DigitalMoneyHouse.msvc_accounts.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createAccount(@PathVariable("userId") Long userId) {
        try {
            accountService.createAccount(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cuenta creada con éxito.");
        } catch (Exception e) {
            // Log the exception if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la cuenta.");
        }
    }

}