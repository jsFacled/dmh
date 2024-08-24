package com.DigitalMoneyHouse.msvc_transactions.service;

import com.DigitalMoneyHouse.msvc_transactions.exceptions.TransactionNotFoundException;
import com.DigitalMoneyHouse.msvc_transactions.models.dto.TransactionDTO;
import com.DigitalMoneyHouse.msvc_transactions.models.entity.Transaction;
import com.DigitalMoneyHouse.msvc_transactions.repository.ITransactionRepository;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public List<TransactionDTO> findLastFiveTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public TransactionDTO getTransactionById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid transaction ID provided: " + id);
        }

        return transactionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new TransactionNotFoundException("Transacción con id **" + id + "** no encontrada"));
    }




    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    public ResponseEntity<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        // Convertir el DTO a una entidad Transaction
        Transaction transaction = convertToEntity(transactionDTO);

        // Guardar la entidad en la base de datos
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Convertir la entidad guardada a DTO y devolverla
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedTransaction));
    }

    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            // Actualizar los campos de la transacción
            transaction.setDate(transactionDTO.getDate());
            transaction.setOriginAccountId(transactionDTO.getOriginAccountId());
            transaction.setType(transactionDTO.getType());
            transaction.setProductOriginType(transactionDTO.getProductOriginType());
            transaction.setProductOriginId(transactionDTO.getProductOriginId());
            transaction.setDestinationAccountId(transactionDTO.getDestinationAccountId());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDescription(transactionDTO.getDescription());

            // Guardar la transacción actualizada
            Transaction updatedTransaction = transactionRepository.save(transaction);

            // Devolver el DTO de la transacción actualizada
            return convertToDTO(updatedTransaction);
        } else {
            throw new NotFoundException("Transaction not found with id " + id);
        }
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    // Método para convertir una entidad Transaction a un DTO
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
                transaction.getDescription()
        );
    }

    // Método para convertir un DTO a una entidad Transaction
    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setDate(transactionDTO.getDate());
        transaction.setOriginAccountId(transactionDTO.getOriginAccountId());
        transaction.setType(transactionDTO.getType());
        transaction.setProductOriginType(transactionDTO.getProductOriginType());
        transaction.setProductOriginId(transactionDTO.getProductOriginId());
        transaction.setDestinationAccountId(transactionDTO.getDestinationAccountId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        return transaction;
    }
}
