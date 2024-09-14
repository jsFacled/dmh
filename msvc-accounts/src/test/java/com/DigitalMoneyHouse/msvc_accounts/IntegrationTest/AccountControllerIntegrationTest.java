package com.DigitalMoneyHouse.msvc_accounts.IntegrationTest;




import com.DigitalMoneyHouse.msvc_accounts.dto.AccountDTO;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountMapper;
import com.DigitalMoneyHouse.msvc_accounts.dto.AccountResponseDTO;
import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import com.DigitalMoneyHouse.msvc_accounts.exceptions.AccountNotFoundException;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import com.DigitalMoneyHouse.msvc_accounts.service.AccountService;
import com.DigitalMoneyHouse.msvc_accounts.service.AliasGeneratorService;
import com.DigitalMoneyHouse.msvc_accounts.service.CVUGeneratorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.reflect.Array.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testGetAccountBalance_Success() throws Exception {
        Long accountId = 1L;
        BigDecimal expectedBalance = new BigDecimal("100.00");

        Mockito.when(accountService.getAccountBalance(accountId)).thenReturn(expectedBalance);

        mockMvc.perform((RequestBuilder) get("/accounts/{id}/balance", Math.toIntExact(accountId)))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void testGetAccountBalance_AccountNotFound() throws Exception {
        Long accountId = 1L;

        Mockito.when(accountService.getAccountBalance(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform((RequestBuilder) get("/accounts/{id}/balance", Math.toIntExact(accountId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAccount_Success() throws Exception {
        Long userId = 1L;
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        //accountResponseDTO.setCvu("1234567890");
        //accountResponseDTO.setAlias("alias.test");
 AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCvu("1234567890");
        accountDTO.setAlias("alias.test");

        Mockito.when(accountService.createAccount(userId)).thenReturn(accountResponseDTO);

        mockMvc.perform((RequestBuilder) post("/accounts/create/{userId}", userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cvu").value("1234567890"))
                .andExpect(jsonPath("$.alias").value("alias.test"));
    }

    @Test
    public void testPatchAccount_Success() throws Exception {
        Long accountId = 1L;
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("200.00"));
        accountDTO.setCvu("1234567890");
        accountDTO.setAlias("alias.updated");

        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", "200.00");

        Mockito.when(accountService.patchAccount(accountId, updates)).thenReturn(accountDTO);

        mockMvc.perform(patch("/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":\"200.00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("200.00"))
                .andExpect(jsonPath("$.alias").value("alias.updated"));
    }

    @Test
    public void testPatchAccount_AccountNotFound() throws Exception {
        Long accountId = 1L;

        Mockito.when(accountService.patchAccount(Mockito.eq(accountId), Mockito.anyMap())).thenThrow(new EntityNotFoundException("Account not found"));

        mockMvc.perform(patch("/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":\"200.00\"}"))
                .andExpect(status().isNotFound());
    }
}
