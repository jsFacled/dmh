package com.DigitalMoneyHouse.msvc_accounts.client.transactions;


import com.DigitalMoneyHouse.msvc_accounts.client.cards.cardsFeign.ICardFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.TransactionDTO;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.ProductOriginType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.models.enums.TransactionType;
import com.DigitalMoneyHouse.msvc_accounts.client.transactions.transactionsFeign.ITransactionFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import com.DigitalMoneyHouse.msvc_accounts.repository.IAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionClientService {


    private final ITransactionFeignClient transactionFeignClient;
    private final ICardFeignClient cardFeignClient;
    private final IAccountRepository accountRepository;

    private ProductOriginType productOriginType;


    public TransactionClientService(ITransactionFeignClient transactionFeignClient, ICardFeignClient cardFeignClient, IAccountRepository accountRepository) {
        this.transactionFeignClient = transactionFeignClient;
        this.cardFeignClient = cardFeignClient;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public ResponseEntity<?> executeTransfer(TransactionDTO transactionDTO) {
        validateTransactionDetails(transactionDTO);
        debitAndCreditAccounts(transactionDTO);
        validateDateAndDescription(transactionDTO);

        return transactionFeignClient.createTransaction(transactionDTO);
    }

    private void validateTransactionDetails(TransactionDTO transactionDTO) {
        // 1. Validar si la cuenta de origen existe
        if (!accountRepository.existsById(transactionDTO.getOriginAccountId())) {
            throw new IllegalArgumentException("Cuenta de origen no encontrada");
        }

        // 2. Validar el tipo de transacción y el origen del producto
        if (transactionDTO.getType() == TransactionType.TRANSFER_BETWEEN_ACCOUNTS) {
            productOriginType = ProductOriginType.ACCOUNT;
            if (transactionDTO.getProductOriginType() != ProductOriginType.ACCOUNT) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de origen del producto debe ser ACCOUNT para TRANSFER_BETWEEN_ACCOUNTS");
            }
            // Validar si el ID del producto de origen coincide con la cuenta de origen
            if (!transactionDTO.getProductOriginId().equals(transactionDTO.getOriginAccountId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la cuenta de origen no coincide con el ID del producto de origen");
            }
        } else if (transactionDTO.getType() == TransactionType.RECHARGE_FROM_CARD) {
            productOriginType = ProductOriginType.CARD;
            if (transactionDTO.getProductOriginType() != ProductOriginType.CARD) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de origen del producto debe ser CARD para RECHARGE_FROM_CARD");
            }
            // Validar si el ID del producto de origen coincide con el ID de la tarjeta proporcionado
            CardRequestDTO card = cardFeignClient.getCardById(transactionDTO.getProductOriginId()).getBody();
            if (card == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarjeta no encontrada para el ID proporcionado");
            }

            // **Nueva Validación:** Verificar que la tarjeta esté asociada a la cuenta de origen
            boolean isAssociated = Boolean.TRUE.equals(cardFeignClient.isCardAssociatedWithAccount(card.getId(), transactionDTO.getOriginAccountId()).getBody());

            if (!isAssociated) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La tarjeta no está asociada a la cuenta de origen");
            }

            // Validar según el tipo de tarjeta
            if (card.getCardType() == CardType.CREDIT) {
                // Validar límite de crédito disponible
                if (card.getCreditLimit().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Límite de crédito insuficiente");
                }
            } else if (card.getCardType() == CardType.DEBIT) {
                // Validar saldo disponible
                if (card.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente en la tarjeta de débito");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de tarjeta no soportado");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de transacción no soportado");
        }

        // 3. Validar si la cuenta de destino existe
        if (!accountRepository.existsById(transactionDTO.getDestinationAccountId())) {
            throw new IllegalArgumentException("Cuenta de destino no encontrada");
        }
    }

    private void debitAndCreditAccounts(TransactionDTO transactionDTO) {
        boolean sufficientFunds;

        // 1. Verificar Fondos Disponibles en Origen
        if (transactionDTO.getProductOriginType() == ProductOriginType.ACCOUNT) {
            // Origen es una cuenta bancaria
            int rowsAffected = accountRepository.decreaseBalanceIfSufficient(transactionDTO.getOriginAccountId(), transactionDTO.getAmount());
            if (rowsAffected == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fondos insuficientes en la cuenta de origen");
            }
        } else if (transactionDTO.getProductOriginType() == ProductOriginType.CARD) {
            // Origen es una tarjeta
            CardRequestDTO card = cardFeignClient.getCardById(transactionDTO.getProductOriginId()).getBody();
            if (card == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarjeta no encontrada");
            }

            if (card.getCardType() == CardType.CREDIT) {
                // Verificar límite de crédito disponible
                sufficientFunds = card.getCreditLimit().compareTo(transactionDTO.getAmount()) >= 0;
                if (!sufficientFunds) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Límite de crédito insuficiente");
                }
                // Debitar del límite de crédito
                cardFeignClient.decreaseCreditLimit(card.getId(), transactionDTO.getAmount());
            } else if (card.getCardType() == CardType.DEBIT) {
                // Verificar saldo disponible
                sufficientFunds = card.getBalance().compareTo(transactionDTO.getAmount()) >= 0;
                if (!sufficientFunds) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente en la tarjeta de débito");
                }
                // Debitar del saldo de la tarjeta
                cardFeignClient.decreaseDebitCardBalance(card.getId(), transactionDTO.getAmount());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de origen del producto no soportado");
        }

        // 2. Aumentar Balance en la Cuenta de Destino
        int rowsAffected = accountRepository.increaseBalance(transactionDTO.getDestinationAccountId(), transactionDTO.getAmount());
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al aumentar el balance en la cuenta de destino");
        }

        // 3. Transacción exitosa
        System.out.println("Transacción exitosa");
    }



    private void validateDateAndDescription (TransactionDTO transactionDTO){

        // Fecha: Si no se proporciona, se asigna la fecha y hora actual.
        if (transactionDTO.getDate() == null) {
            transactionDTO.setDate(LocalDateTime.now());
        }

        // Descripción: Si no se proporciona, generar una por defecto
        if (transactionDTO.getDescription() == null|| transactionDTO.getDescription().trim().isEmpty()) {
            if(productOriginType == ProductOriginType.ACCOUNT) {
                transactionDTO.setDescription(
                        transactionDTO.getType().getDescription() +
                                " de Cvu " + accountRepository.getCvuById(transactionDTO.getProductOriginId()) +
                                " a Cvu" + accountRepository.getCvuById(transactionDTO.getDestinationAccountId()));
            }

            if(productOriginType == ProductOriginType.CARD) {
                transactionDTO.setDescription(
                        transactionDTO.getType().getDescription() +
                                " de Card número " + cardFeignClient.getNumberById(transactionDTO.getProductOriginId()) +
                                " a Cvu " + accountRepository.getCvuById(transactionDTO.getDestinationAccountId()));
            }
        }
    }



    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(Long accountId) {
        return transactionFeignClient.getTransactionsByAccountId(accountId);
    }

    public ResponseEntity<TransactionDTO> getTransactionById(Long transactionId) {
        return transactionFeignClient.getTransactionById(transactionId);
    }
}
