package com.DigitalMoneyHouse.msvc_accounts.client.transactions;


import com.DigitalMoneyHouse.msvc_accounts.client.cards.cardsFeign.ICardFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.TransactionDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.TransactionType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign.ITransactionFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;


@Service
public class TransactionClientService {


    private final ITransactionFeignClient transactionFeignClient;
    private final ICardFeignClient cardFeignClient;
    private final IAccountRepository accountRepository;
    private BigDecimal originBalance;
    private BigDecimal destinationBalance;
    private Account originAccount;
    private Account destinationAccount;

    public TransactionClientService(ITransactionFeignClient transactionFeignClient, ICardFeignClient cardFeignClient, IAccountRepository accountRepository) {
        this.transactionFeignClient = transactionFeignClient;
        this.cardFeignClient = cardFeignClient;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<?> executeTransfer(TransactionDTO transactionDTO) {
        validateTransaction(transactionDTO);
        debitAndCreditAccounts(transactionDTO);

        return transactionFeignClient.createTransaction(transactionDTO);
    }


    private void validateTransaction(TransactionDTO transactionDTO) {

        // 1. Validar si la cuenta de origen existe
        if (!accountRepository.existsById(transactionDTO.getOriginAccountId())) {
            throw new IllegalArgumentException("Cuenta de origen no encontrada");
        }


        // 2. Validar el tipo de transacción y el origen del producto
        if (transactionDTO.getType() == TransactionType.TRANSFER_BETWEEN_ACCOUNTS) {
            if (transactionDTO.getProductOriginType() != ProductOriginType.ACCOUNT) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de origen del producto debe ser ACCOUNT para TRANSFER_BETWEEN_ACCOUNTS");
            }
            // Validar si el ID del producto de origen coincide con la cuenta de origen
            if (!transactionDTO.getProductOriginId().equals(transactionDTO.getOriginAccountId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la cuenta de origen no coincide con el ID del producto de origen");
            }
        } else if (transactionDTO.getType() == TransactionType.RECHARGE_FROM_CARD) {
            if (transactionDTO.getProductOriginType() != ProductOriginType.CARD) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de origen del producto debe ser CARD para RECHARGE_FROM_CARD");
            }
            // Validar si el ID del producto de origen coincide con el ID de la tarjeta proporcionado
            if (!cardFeignClient.existsById(transactionDTO.getProductOriginId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarjeta no encontrada para el ID proporcionado");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de transacción no soportado");
        }

        // 3. Validar si la cuenta de destino existe
        if (!accountRepository.existsById(transactionDTO.getDestinationAccountId())) {
            throw new IllegalArgumentException("Cuenta de destino no encontrada");
        }

    }

    private void debitAndCreditAccounts(TransactionDTO transactionDTO){
// 1. Verificar Fondos Disponibles en Origen
        boolean isFundsSufficient = accountRepository.decreaseBalanceIfSufficient(transactionDTO.getOriginAccountId(), transactionDTO.getAmount());

        if (!isFundsSufficient) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fondos insuficientes en la cuenta de origen");
        }

        // 2. Aumentar Balance en la Cuenta de Destino
        boolean isBalanceIncreased = accountRepository.increaseBalance(transactionDTO.getDestinationAccountId(), transactionDTO.getAmount());

        if (!isBalanceIncreased) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al aumentar el balance en la cuenta de destino");
        }

        // 3. Transacción exitosa
        System.out.println("Transacción exitosa");
    }

}
