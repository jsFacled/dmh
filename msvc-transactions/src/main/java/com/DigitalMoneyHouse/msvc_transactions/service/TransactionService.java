package com.DigitalMoneyHouse.msvc_transactions.service;


import com.DigitalMoneyHouse.msvc_transactions.models.dto.TransactionDTO;
import com.DigitalMoneyHouse.msvc_transactions.models.entity.Transaction;
import com.DigitalMoneyHouse.msvc_transactions.repository.ITransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final ITransactionRepository transactionRepository;

    public TransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
/*
    public List<TransactionDTO> findLastFiveTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
*/

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getDate(),
              transaction.getOriginAccountId(),
                transaction.getType(),
                transaction.getProductOriginType(),
                transaction.getProductOriginId(),
                transaction.getDestinationAccountId(),
                transaction.getAmount(),
                transaction.getDescription());
    }





    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    /*
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }
*/
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            transaction.setId(transactionDetails.getId());
            transaction.setDate(transactionDetails.getDate());
            transaction.setOriginAccountId(transaction.getOriginAccountId());
            transaction.setType(transactionDetails.getType());

            transaction.setProductOriginType(transaction.getProductOriginType());
            transaction.setProductOriginId(transaction.getProductOriginId());

            transaction.setDestinationAccountId(transaction.getDestinationAccountId());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDescription(transactionDetails.getDescription());

        return transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Transaction not found with id " + id);
        }
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
