package com.DigitalMoneyHouse.msvc_accounts.UnitTest;


import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private IAccountRepository accountRepository;

    @Test
    public void testFindBalanceByAccountId() {
        Account account = new Account();
        account.setBalance(new BigDecimal("150.00"));
        account = accountRepository.save(account);

        BigDecimal balance = accountRepository.findBalanceByAccountId(account.getId());
        assertNotNull(balance);
        assertEquals(new BigDecimal("150.00"), balance);
    }

    @Test
    public void testDecreaseBalanceIfSufficient() {
        Account account = new Account();
        account.setBalance(new BigDecimal("200.00"));
        account = accountRepository.save(account);

        int updated = accountRepository.decreaseBalanceIfSufficient(account.getId(), new BigDecimal("50.00"));
        assertEquals(1, updated);

        Account updatedAccount = accountRepository.findById(account.getId()).get();
        assertEquals(new BigDecimal("150.00"), updatedAccount.getBalance());
    }
}
