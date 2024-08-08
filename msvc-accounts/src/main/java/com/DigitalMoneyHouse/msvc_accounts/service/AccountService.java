package com.DigitalMoneyHouse.msvc_accounts.service;


import com.DigitalMoneyHouse.msvc_accounts.dto.AccountDTO;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountMapper;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountResponseDTO;
import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AliasGeneratorService aliasGeneratorService;
    private final CVUGeneratorService cvuGeneratorService;

    public AccountService(IAccountRepository accountRepository, AccountMapper accountMapper, AliasGeneratorService aliasGeneratorService, CVUGeneratorService cvuGeneratorService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.aliasGeneratorService = aliasGeneratorService;
        this.cvuGeneratorService = cvuGeneratorService;
    }

    public AccountResponseDTO createAccount(Long user_id) {

        Account account = new Account();
        account.setUserId(user_id);
        account.setBalance(BigDecimal.valueOf(0));

        account.setCvu(cvuGeneratorService.generateCVU());
        account.setAlias(aliasGeneratorService.generateAlias());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountResponseDTO(savedAccount);
    }

    public AccountDTO getAccount(Long account_id) {
        Optional<Account> accountOptional = accountRepository.findById(account_id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // Crear y llenar el DTO
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setBalance(account.getBalance());
            accountDTO.setCvu(account.getCvu());
            accountDTO.setAlias(account.getAlias());

            return accountDTO;
        } else {
            throw new EntityNotFoundException("Account with id " + account_id + " not found");
        }
    }

    public AccountDTO patchAccount(Long accountId, Map<String, Object> updates) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "balance":
                        account.setBalance(new BigDecimal(value.toString()));
                        break;
                    case "cvu":
                        account.setCvu((String) value);
                        break;
                    case "alias":
                        account.setAlias((String) value);
                        break;
                    // Añadir casos para otros campos si es necesario
                }
            });
            account.setUpdatedAt(LocalDateTime.now());
            Account updatedAccount = accountRepository.save(account);
            return accountMapper.toAccountDTO(updatedAccount);
        } else {
            throw new EntityNotFoundException("Account with id " + accountId + " not found");
        }
    }
}
