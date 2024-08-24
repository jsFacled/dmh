package com.DigitalMoneyHouse.msvc_accounts.client.transactions;

import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-transactions")
public interface ITransactionFeignClient {

    @PostMapping("/transactions")
    ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO);

    @GetMapping("/accounts/{accountId}/transactions")
    ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(@PathVariable("accountId") Long accountId);

    @GetMapping("/accounts/{accountId}/transactions/{transactionId}")
    ResponseEntity<TransactionDTO> getTransactionById(@PathVariable("accountId") Long accountId, @PathVariable("transactionId") Long transactionId);
}