package com.DigitalMoneyHouse.msvc_accounts.controller;

import com.DigitalMoneyHouse.msvc_accounts.dto.AccountResponseDTO;
import com.DigitalMoneyHouse.msvc_accounts.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createAccount(@PathVariable("userId") Long userId) {
        try {
            AccountResponseDTO accountresponseDTO = accountService.createAccount(userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(accountresponseDTO);
        } catch (Exception e) {
            logger.error("Error al crear la cuenta para el usuario con ID: " + userId, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la cuenta.");
        }
    }

}