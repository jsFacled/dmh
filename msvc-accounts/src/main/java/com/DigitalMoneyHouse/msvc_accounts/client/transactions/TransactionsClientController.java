package com.DigitalMoneyHouse.msvc_accounts.client.transactions;


import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.TransactionDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign.ITransactionFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * No se utiliza un Service en ms-accounts.client.transactions porque el Controller simplemente delega solicitudes
 *  al microservicio ms-transactions a través de OpenFeign, sin agregar lógica adicional.
 *  Esto simplifica el diseño, evita capas innecesarias y aprovecha la capacidad de OpenFeign para manejar la
 *  comunicación entre microservicios de manera directa y eficiente.
 */
@RestController
@RequestMapping("/accounts/{accountId}")
public class TransactionsClientController {

    private final ITransactionFeignClient iTransactionFeignClient;

    public TransactionsClientController(ITransactionFeignClient iTransactionFeignClient) {
        this.iTransactionFeignClient = iTransactionFeignClient;
    }
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(
            @PathVariable Long accountId,
            @RequestBody TransactionDTO transactionDTO) {

        // Ensure that the accountId in the transactionDTO matches the path variable
        transactionDTO.setOriginAccountId(accountId);

        return iTransactionFeignClient.createTransaction(transactionDTO);
    }

    @GetMapping("activity")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(
            @PathVariable Long accountId) {

        return iTransactionFeignClient.getTransactionsByAccountId(accountId);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @PathVariable Long accountId,
            @PathVariable Long transactionId) {

        return iTransactionFeignClient.getTransactionById(accountId, transactionId);
    }
}