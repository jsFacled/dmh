package com.DigitalMoneyHouse.msvc_accounts.Cient;


import com.DigitalMoneyHouse.msvc_accounts.client.transactions.TransactionClientService;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.TransactionDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.TransactionType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign.ITransactionFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionClientServiceTest {

    @MockBean
    private ITransactionFeignClient transactionFeignClient;

    @MockBean
    private IAccountRepository accountRepository;

    @InjectMocks
    private TransactionClientService transactionClientService;

    @Test
    public void testGetTransactionById_success() {
        // Dado: una transacción existente
        Long transactionId = 123L;
        TransactionDTO mockTransaction = new TransactionDTO(transactionId, LocalDateTime.now(), 1L, TransactionType.TRANSFER_BETWEEN_ACCOUNTS, ProductOriginType.ACCOUNT, 1L, 2L, BigDecimal.TEN, "Transferencia");
        when(transactionFeignClient.getTransactionById(transactionId))
                .thenReturn(ResponseEntity.ok(mockTransaction));

        // Cuando: se llama al método getTransactionById
        ResponseEntity<TransactionDTO> response = transactionClientService.getTransactionById(transactionId);

        // Entonces: el resultado debe ser correcto
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTransaction, response.getBody());
    }

    @Test
    public void testGetTransactionById_notFound() {
        // Dado: una transacción inexistente
        Long transactionId = 999L;
        when(transactionFeignClient.getTransactionById(transactionId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transacción no encontrada"));

        // Cuando: se intenta obtener la transacción
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionClientService.getTransactionById(transactionId);
        });

        // Entonces: se lanza una excepción con el estado 404
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Transacción no encontrada", exception.getReason());
    }

    @Test
    public void testGetTransactionById_accountNotFound() {
        // Dado: una cuenta inexistente para la transacción
        Long transactionId = 123L;
        when(transactionFeignClient.getTransactionById(transactionId))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuenta no encontrada"));

        // Cuando: se intenta obtener la transacción
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionClientService.getTransactionById(transactionId);
        });

        // Entonces: se lanza una excepción con el estado 400
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Cuenta no encontrada", exception.getReason());
    }
}
