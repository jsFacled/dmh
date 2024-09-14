package com.DigitalMoneyHouse.msvc_transactions.controller;

import com.DigitalMoneyHouse.msvc_transactions.models.dto.TransactionDTO;
import com.DigitalMoneyHouse.msvc_transactions.models.entity.Transaction;
import com.DigitalMoneyHouse.msvc_transactions.service.TransactionService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

   private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping("/hello")
    public String sayHello() {
        return "Hello desde ms-transactions";
    }

    @GetMapping("/last-five-destinations/{accountId}")
    public ResponseEntity<?> getLastFiveDistinctDestinationsByAccountId(
            @PathVariable("accountId") Long accountId) {
        List<Long> destinationAccountIds = transactionService.findDistinctDestinationAccountIds(accountId);
        return ResponseEntity.ok(destinationAccountIds);
    }

    @GetMapping("/last-five/{account_id}")
    public ResponseEntity<List<TransactionDTO>> getLastFiveTransactionsByAccountId(@PathVariable Long account_id) {
        List<TransactionDTO> transactions = transactionService.findLastFiveTransactionsByAccountId(account_id);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }


       @GetMapping("/account/{accountId}")
    public List<TransactionDTO> getTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        try {
            TransactionDTO transactionDTO = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transactionDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO updatedTransactionDTO = transactionService.updateTransaction(id, transactionDTO);
            return ResponseEntity.ok(updatedTransactionDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
