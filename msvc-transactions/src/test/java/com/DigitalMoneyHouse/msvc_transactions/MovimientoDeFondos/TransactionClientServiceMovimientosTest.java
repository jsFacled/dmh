package com.DigitalMoneyHouse.msvc_transactions.MovimientoDeFondos;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionClientServiceMovimientosTest {

    @Mock
    private ITransactionFeignClient transactionFeignClient;

    @Mock
    private ICardFeignClient cardFeignClient;

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private TransactionClientService transactionClientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecuteTransfer_Success() {
        TransactionDTO transactionDTO = new TransactionDTO(
                1L, LocalDateTime.now(), 1L, TransactionType.RECHARGE_FROM_CARD,
                ProductOriginType.CARD, 1L, 2L, BigDecimal.valueOf(100), "Test transaction"
        );

        CardRequestDTO card = new CardRequestDTO();
        card.setBalance(BigDecimal.valueOf(200));
        card.setCreditLimit(BigDecimal.valueOf(500));

        when(cardFeignClient.getCardById(transactionDTO.getProductOriginId())).thenReturn(ResponseEntity.ok(card));
        when(accountRepository.existsById(transactionDTO.getOriginAccountId())).thenReturn(true);
        when(accountRepository.existsById(transactionDTO.getDestinationAccountId())).thenReturn(true);
        when(accountRepository.decreaseBalanceIfSufficient(transactionDTO.getOriginAccountId(), transactionDTO.getAmount())).thenReturn(1);
        when(accountRepository.increaseBalance(transactionDTO.getDestinationAccountId(), transactionDTO.getAmount())).thenReturn(1);
        when(transactionFeignClient.createTransaction(transactionDTO)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = transactionClientService.executeTransfer(transactionDTO);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testExecuteTransfer_InsufficientFunds() {
        TransactionDTO transactionDTO = new TransactionDTO(
                1L, LocalDateTime.now(), 1L, TransactionType.RECHARGE_FROM_CARD,
                ProductOriginType.CARD, 1L, 2L, BigDecimal.valueOf(100), "Test transaction"
        );

        CardRequestDTO card = new CardRequestDTO();
        card.setBalance(BigDecimal.valueOf(50));
        card.setCreditLimit(BigDecimal.valueOf(500));

        when(cardFeignClient.getCardById(transactionDTO.getProductOriginId())).thenReturn(ResponseEntity.ok(card));
        when(accountRepository.existsById(transactionDTO.getOriginAccountId())).thenReturn(true);
        when(accountRepository.existsById(transactionDTO.getDestinationAccountId())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            transactionClientService.executeTransfer(transactionDTO);
        });
    }

    @Test
    public void testExecuteTransfer_AccountNotFound() {
        TransactionDTO transactionDTO = new TransactionDTO(
                1L, LocalDateTime.now(), 1L, TransactionType.RECHARGE_FROM_CARD,
                ProductOriginType.CARD, 1L, 2L, BigDecimal.valueOf(100), "Test transaction"
        );

        when(cardFeignClient.getCardById(transactionDTO.getProductOriginId())).thenReturn(ResponseEntity.ok(new CardRequestDTO()));
        when(accountRepository.existsById(transactionDTO.getOriginAccountId())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            transactionClientService.executeTransfer(transactionDTO);
        });
    }
}
