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
    public ResponseEntity<AccountResponseDTO> createAccount(@PathVariable("userId") Long userId) {
        try {
            AccountResponseDTO responseDTO = accountService.createAccount(userId);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception and return an appropriate HTTP status code
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}