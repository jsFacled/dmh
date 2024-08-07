package com.DigitalMoneyHouse.msvc_transactions.service;


import com.DigitalMoneyHouse.msvc_transactions.repository.ITransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    public List<TransactionDTO> findLastFiveTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getAccountId(), transaction.getAmount(), transaction.getType(), transaction.getDescription(), transaction.getDate());
    }
}
