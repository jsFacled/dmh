package com.DigitalMoneyHouse.msvc_accounts.service;


import com.DigitalMoneyHouse.msvc_accounts.dto.AccountDTO;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountMapper;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountResponseDTO;
import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
