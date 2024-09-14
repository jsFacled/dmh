package com.DigitalMoneyHouse.msvc_accounts.Cient;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.cardsFeign.ICardFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.TransactionClientService;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.TransactionDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.TransactionType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign.ITransactionFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
class TransactionClientServiceExcecuteTest {

    @Mock
    private ITransactionFeignClient transactionFeignClient;
    @Mock
    private ICardFeignClient cardFeignClient;
    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private TransactionClientService transactionClientService;

    @Test
    void testExecuteTransferSuccess() {
        // Setup
        Long originAccountId = 1L;
        Long destinationAccountId = 2L;
        TransactionDTO transactionDTO = new TransactionDTO(
                null, LocalDateTime.now(), originAccountId, TransactionType.TRANSFER_BETWEEN_ACCOUNTS,
                ProductOriginType.ACCOUNT, originAccountId, destinationAccountId, BigDecimal.valueOf(100), "Test transfer");

        when(accountRepository.existsById(originAccountId)).thenReturn(true);
        when(accountRepository.existsById(destinationAccountId)).thenReturn(true);
        when(accountRepository.decreaseBalanceIfSufficient(anyLong(), any(BigDecimal.class))).thenReturn(1);
        when(accountRepository.increaseBalance(anyLong(), any(BigDecimal.class))).thenReturn(1);

        // Execute
        ResponseEntity<?> response = transactionClientService.executeTransfer(transactionDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testExecuteTransferInsufficientFunds() {
        // Setup
        Long originAccountId = 1L;
        Long destinationAccountId = 2L;
        TransactionDTO transactionDTO = new TransactionDTO(
                null, LocalDateTime.now(), originAccountId, TransactionType.TRANSFER_BETWEEN_ACCOUNTS,
                ProductOriginType.ACCOUNT, originAccountId, destinationAccountId, BigDecimal.valueOf(100), "Test transfer");

        when(accountRepository.existsById(originAccountId)).thenReturn(true);
        when(accountRepository.existsById(destinationAccountId)).thenReturn(true);
        when(accountRepository.decreaseBalanceIfSufficient(anyLong(), any(BigDecimal.class))).thenReturn(0);

        // Execute
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                transactionClientService.executeTransfer(transactionDTO));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Fondos insuficientes en la cuenta de origen", thrown.getReason());
    }

    // Additional tests for other cases
}
