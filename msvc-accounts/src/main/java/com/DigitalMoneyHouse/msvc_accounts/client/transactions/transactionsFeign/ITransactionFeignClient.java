package com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign;

import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.dto.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-transactions", url = "http://localhost:8084", configuration = TransactionsFeignConfig.class)
public interface ITransactionFeignClient {

    @PostMapping("/transactions")
    ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO);

    @GetMapping("/transactions/account/{accountId}")
    ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(@PathVariable("accountId") Long accountId);

    @GetMapping("/transactions/{id}")
    ResponseEntity<TransactionDTO> getTransactionById(@PathVariable("id") Long id);

    //Este cliente llama al endpoint en ms-transactions y devuelve una lista de destinationAccountId.
    @GetMapping("/transactions/last-five-destinations/{accountId}")
    List<Long> getLastFiveDistinctDestinationsByAccountId(@PathVariable("accountId") Long accountId);
}
