package com.DigitalMoneyHouse.msvc_accounts.dto;

import com.DigitalMoneyHouse.msvc_accounts.entity.Account;

import com.DigitalMoneyHouse.msvc_accounts.dto.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

AccountDTO a = new AccountDTO();

 /*
    // Convierte Account a AccountDTO
    public AccountDTO toAccountDTO(Account account) {
        if (account == null) {
            return null;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setUserId(account.getUser().getId());
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
        accountResponseDTO.setUserId(account.getUser().getId());
        accountResponseDTO.setBalance(account.getBalance());
        accountResponseDTO.setCvu(account.getCvu());
        accountResponseDTO.setAlias(account.getAlias());
        return accountResponseDTO;
    }

*/
}

