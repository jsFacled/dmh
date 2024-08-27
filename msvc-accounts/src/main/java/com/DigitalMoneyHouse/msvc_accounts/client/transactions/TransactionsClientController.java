package com.DigitalMoneyHouse.msvc_accounts.client.transactions;


import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.DestinationAccountInfoDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.TransactionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
private final TransactionClientService transactionClientService;

    public TransactionsClientController(TransactionClientService transactionClientService) {
        this.transactionClientService = transactionClientService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> executeTransfer(
            @PathVariable Long accountId,
            @RequestBody TransactionDTO transactionDTO) {

        // Ensure that the accountId in the transactionDTO matches the path variable
        transactionDTO.setOriginAccountId(accountId);

        return transactionClientService.executeTransfer(transactionDTO);
    }

    @GetMapping("activity")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(
            @PathVariable Long accountId) {

        return transactionClientService.getTransactionsByAccountId(accountId);
    }

    @GetMapping("/activity/{transactionId}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long transactionId) {

        try {
            return transactionClientService.getTransactionById(transactionId);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor desde controller feign");
        }
    }

    @GetMapping("/transferences")
    public List<DestinationAccountInfoDTO> getLastFiveRecipients(@PathVariable Long accountId) {
        return transactionClientService.getLastFiveRecipientsInfo(accountId);
    }
}