package com.DigitalMoneyHouse.msvc_accounts.Cient;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.DigitalMoneyHouse.msvc_accounts.client.transactions.TransactionClientService;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.TransactionsClientController;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.DestinationAccountInfoDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.TransactionDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@WebMvcTest(TransactionsClientController.class)
class TransactionsClientControllerTest {

    @Mock
    private TransactionClientService transactionClientService;

    @InjectMocks
    private TransactionsClientController transactionsClientController;

    @Test
    void testGetLastFiveRecipients() {
        // Setup
        Long accountId = 1L;
        List<DestinationAccountInfoDTO> expected = Collections.singletonList(new DestinationAccountInfoDTO(1L, "CVU123", "Alias1"));

        when(transactionClientService.getLastFiveRecipientsInfo(accountId)).thenReturn(expected);

        // Execute
        List<DestinationAccountInfoDTO> actual = transactionsClientController.getLastFiveRecipients(accountId);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testExecuteTransferSuccess() {
        // Setup
        Long accountId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO(null, LocalDateTime.now(), accountId, TransactionType.TRANSFER_BETWEEN_ACCOUNTS,
                ProductOriginType.ACCOUNT, accountId, 2L, BigDecimal.valueOf(100), "Test transfer");

        when(transactionClientService.executeTransfer(transactionDTO)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Execute
        ResponseEntity<?> response = transactionsClientController.executeTransfer(accountId, transactionDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Additional tests for other endpoints
}
