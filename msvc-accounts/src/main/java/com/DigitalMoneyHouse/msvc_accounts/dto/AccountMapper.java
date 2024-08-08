package com.DigitalMoneyHouse.msvc_accounts.dto;

import com.DigitalMoneyHouse.msvc_accounts.entity.Account;

import com.DigitalMoneyHouse.msvc_accounts.dto.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    // Convierte Account a AccountDTO
    public AccountDTO toAccountDTO(Account account) {
        if (account == null) {
            return null;
        }


        AccountDTO accountDTO = new AccountDTO();



        accountDTO.setBalance(account.getBalance());
        accountDTO.setCvu(account.getCvu());
        accountDTO.setAlias(account.getAlias());

        return accountDTO;
    }


    // Convierte Account a AccountResponseDTO
    public AccountResponseDTO toAccountResponseDTO(Account account) {
        if (account == null) {
            return null;
        }
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();

        accountResponseDTO.setId(account.getId());
        accountResponseDTO.setUserId(account.getUserId());

        return accountResponseDTO;
    }



}

