package com.DigitalMoneyHouse.msvc_accounts.controller;

import com.DigitalMoneyHouse.msvc_accounts.dto.AccountDTO;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountResponseDTO;
import com.DigitalMoneyHouse.msvc_accounts.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable("id") Long accountId) {
        BigDecimal balance = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }

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


    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable("id") Long accountId){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(accountId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO> patchAccount(@PathVariable("id") Long accountId, @RequestBody Map<String, Object> updates) {
        try {
            AccountDTO accountDTO = accountService.patchAccount(accountId, updates);
            return ResponseEntity.status(HttpStatus.OK).body(accountDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}