package com.DigitalMoneyHouse.msvc_accounts.client.transactions;

import com.DigitalMoneyHouse.msvc_accounts.client.cards.cardsFeign.ICardFeignClient;
import com.DigitalMoneyHouse.msvc_accounts.client.cards.models.CardRequestDTO;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

@Service
public class TransactionClientService2 {

    private final ITransactionFeignClient transactionFeignClient;
    private final ICardFeignClient cardFeignClient;
    private final IAccountRepository accountRepository;

    private BigDecimal originBalance;
    private BigDecimal destinationBalance;
    private Account originAccount;
    private Account destinationAccount;

    public TransactionClientService2(ITransactionFeignClient transactionFeignClient, ICardFeignClient cardFeignClient, IAccountRepository accountRepository) {
        this.transactionFeignClient = transactionFeignClient;
        this.cardFeignClient = cardFeignClient;
        this.accountRepository = accountRepository;
    }


    /**
     * /*
     *     //private LocalDateTime date;
     *         * if null -->now
     *     //private Long originAccountId;
     *         * patern
     *     //private TransactionType type;
     *     *  if RECHARGE_FROM_CARD
     *             //private ProductOriginType productOriginType;
     *               *CARD
     *     * if TRANSFER_BETWEEN_ACCOUNTS
     *     //private ProductOriginType productOriginType;
     *              *ACCOUNT
     *    -->  //private Long productOriginId;
     *
     *     //private Long destinationAccountId;
     *         *comprobar si existe
     *     //private BigDecimal amount;
     *         *validar que amount origin tenga saldo(amountOrigin - envìo >=0)
     *         *amountDestino +
     *         *amountOrigin -
     *     //private String description;
     *         *if null --> transactionType nameOrigin to nameDestino
     * * */



    /**
     * Realiza una transferencia entre cuentas.
     * @param transactionDTO Los detalles de la transacción.
     * @return ResponseEntity con el resultado de la creación de la transacción.
     */
    public ResponseEntity<?> executeTransfer(TransactionDTO transactionDTO) {
        BigDecimal originBalance;
        BigDecimal destinationBalance;

// Validaciones
        // 1. Fecha: Si no se proporciona, se asigna la fecha y hora actual.
// 2. Cuenta de Origen: Verificar existencia
        // 3. Tipo de Transacción: Validar
        // 4. Tipo de Origen del Producto: Validar en base al Tipo de Transacción


        // 1. Fecha: Si no se proporciona, se asigna la fecha y hora actual.

        if (transactionDTO.getDate() == null) {
            transactionDTO.setDate(LocalDateTime.now());
        }


        // 2. Cuenta de Origen: Verificar existencia
        if (!accountRepository.existsById(transactionDTO.getOriginAccountId())) {
            throw new IllegalArgumentException("Cuenta de origen no encontrada");
        }


        //Tipo de Transacción (transactionType): establecer el id y el producto de origen
        // 3. Tipo de Transacción: Validar
        if (!Arrays.asList(TransactionType.RECHARGE_FROM_CARD, TransactionType.TRANSFER_BETWEEN_ACCOUNTS).contains(transactionDTO.getType())) {
            throw new IllegalArgumentException("Tipo de transacción inválido");
        }

        // 4. Tipo de Origen del Producto: Validar en base al Tipo de Transacción
        if (transactionDTO.getType() == TransactionType.RECHARGE_FROM_CARD) {
            if (transactionDTO.getProductOriginType() != ProductOriginType.CARD) {
                throw new IllegalArgumentException("Tipo de origen del producto no coincide con el tipo de transacción");
            }
        } else if (transactionDTO.getType() == TransactionType.TRANSFER_BETWEEN_ACCOUNTS) {
            if (transactionDTO.getProductOriginType() != ProductOriginType.ACCOUNT) {
                throw new IllegalArgumentException("Tipo de origen del producto no coincide con el tipo de transacción");
            }
        }

        // 5. ID del Origen del Producto: Verificar existencia
        if (transactionDTO.getProductOriginType() == ProductOriginType.CARD) {
            // Llamada a microservicio de tarjetas (opcional)
            // transactionFeignClient.getCardById(transactionDto.getProductOriginId()); // Ejemplo
        } else if (transactionDTO.getProductOriginType() == ProductOriginType.ACCOUNT) {
            accountRepository.findById(transactionDTO.getProductOriginId())
                    .orElseThrow(() -> new IllegalArgumentException("Cuenta de origen del producto no encontrada"));
        }

        // 6. Cuenta de Destino: Verificar existencia
        Account destinationAccount = accountRepository.findById(transactionDTO.getDestinationAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de destino no encontrada"));

        // 7. Monto: Validar saldo suficiente en cuenta de origen
        if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0 ||
                originAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente en la cuenta de origen");
        }

        // 8. Descripción: Si no se proporciona, generar una por defecto
        if (transactionDTO.getDescription() == null) {
            transactionDTO.setDescription(transactionDTO.getType().getDescription() + " de " +
                    originAccount.getAccountNumber() + " a " + destinationAccount.getAccountNumber());
        }

        // 9. Actualizar Saldos (considerar control de concurrencia)
        originAccount.setBalance(originAccount.getBalance().subtract(transactionDTO.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transactionDTO.getAmount()));
        accountRepository.saveAll(List.of(originAccount, destinationAccount));

        // 10. Registrar Transacción en base de datos (utilizando Feign si se requiere persistencia separada)
        TransactionDTO savedTransaction = transactionFeignClient.createTransaction(transactionDTO).getBody();

        return ResponseEntity.ok(savedTransaction);

        // Actualizaciones y registro de la transacción




        /*********************************************************/
        if (transactionDTO.getType() == TransactionType.RECHARGE_FROM_CARD) {
            // Obtén los detalles de la tarjeta desde el servicio de tarjetas
            ResponseEntity<CardRequestDTO> cardResponse = cardFeignClient.getCardById(transactionDTO.getProductOriginId());

            if (cardResponse.getStatusCode().is2xxSuccessful() && cardResponse.getBody() != null) {
                CardRequestDTO cardDTO = cardResponse.getBody();
                // Obtener el saldo de la tarjeta
                originBalance = cardDTO.getBalance();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo obtener la información de la tarjeta");
            }

        } else if (transactionDTO.getType() == TransactionType.TRANSFER_BETWEEN_ACCOUNTS) {
            // Obtén el saldo de la cuenta de origen
            originBalance = getAccountBalance(transactionDTO.getOriginAccountId());

            // Asegúrate de establecer productOriginId según corresponda
            transactionDTO.setProductOriginId(transactionDTO.getOriginAccountId());
        }

// Aquí puedes continuar con la lógica, como validar que el saldo sea suficiente, etc.


        // Valida que la cuenta de destino exista
        if (transactionDTO.getDestinationAccountId() == null || !isValidAccount(transactionDTO.getDestinationAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta de destino no es válida");
        }

        // Valida el saldo disponible en la cuenta de origen
        originBalance = getAccountBalance(transactionDTO.getOriginAccountId());
        if (originBalance.compareTo(transactionDTO.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fondos insuficientes en la cuenta de origen");
        }

// Ajusta los saldos
        destinationBalance = getAccountBalance(transactionDTO.getDestinationAccountId());
        updateAccountBalance(transactionDTO.getOriginAccountId(), originBalance.subtract(transactionDTO.getAmount()));
        updateAccountBalance(transactionDTO.getDestinationAccountId(), destinationBalance.add(transactionDTO.getAmount()));

        // Genera una descripción si es null
        if (transactionDTO.getDescription() == null || transactionDTO.getDescription().isEmpty()) {
            String originName = getAccountName(transactionDTO.getOriginAccountId());
            String destinationName = getAccountName(transactionDTO.getDestinationAccountId());
            transactionDTO.setDescription(transactionDTO.getType().name() + " from " + originName + " to " + destinationName);
        }


        // Valida los datos de la transacción
        validateTransaction(transactionDTO);

        // Ajusta los saldos según el tipo de transacción
        adjustBalances(transactionDTO);

        // Llama al cliente Feign para crear la transacción
        try {
            return transactionFeignClient.createTransaction(transactionDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear la transacción", e);
        }
    }

    /**
     * Valida los datos de la transacción.
     * @param transactionDTO Los detalles de la transacción.
     */
    private void validateTransaction(TransactionDTO transactionDTO) {
// 1. Validar si la cuenta de origen existe
// 2. Validar el tipo de transacción y el origen del producto
    // Validar si el ID del producto de origen coincide con la cuenta de origen
    // Validar si el ID del producto de origen coincide con el ID de la tarjeta proporcionado
// 3. Validar si la cuenta de destino existe

        // Valida que el monto sea positivo
        if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto de la transacción debe ser positivo");
        }

        // Valida que la cuenta de origen exista
        if (transactionDTO.getOriginAccountId() == null || !isValidAccount(transactionDTO.getOriginAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta de origen no es válida");
        }

        // Valida que la cuenta de destino exista
        if (transactionDTO.getDestinationAccountId() == null || !isValidAccount(transactionDTO.getDestinationAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta de destino no es válida");
        }

        // Valida el saldo disponible en la cuenta de origen
        BigDecimal originBalance = getAccountBalance(transactionDTO.getOriginAccountId());
        if (originBalance.compareTo(transactionDTO.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fondos insuficientes en la cuenta de origen");
        }

        // Asigna la descripción si no se proporciona
        if (transactionDTO.getDescription() == null || transactionDTO.getDescription().isEmpty()) {
            transactionDTO.setDescription("Transferencia de " + transactionDTO.getOriginAccountId() + " a " + transactionDTO.getDestinationAccountId());
        }
    }

    /**
     * Ajusta los saldos de las cuentas según el tipo de transacción.
     * @param transactionDTO Los detalles de la transacción.
     */
    private void adjustBalances(TransactionDTO transactionDTO) {
        // Obtiene y actualiza el saldo de la cuenta de origen
        BigDecimal originBalance = getAccountBalance(transactionDTO.getOriginAccountId());
        updateAccountBalance(transactionDTO.getOriginAccountId(), originBalance.subtract(transactionDTO.getAmount()));

        // Obtiene y actualiza el saldo de la cuenta de destino
        BigDecimal destinationBalance = getAccountBalance(transactionDTO.getDestinationAccountId());
        updateAccountBalance(transactionDTO.getDestinationAccountId(), destinationBalance.add(transactionDTO.getAmount()));
    }

    /**
     * Verifica si una cuenta es válida.
     * @param accountId El ID de la cuenta.
     * @return true si la cuenta es válida, false en caso contrario.
     */
    private boolean isValidAccount(Long accountId) {
        // Implementa la lógica para validar la cuenta
        // Aquí deberías consultar el servicio correspondiente para verificar la existencia de la cuenta
        return true; // Placeholder
    }

    /**
     * Obtiene el saldo de una cuenta.
     * @param accountId El ID de la cuenta.
     * @return El saldo de la cuenta.
     */
    private BigDecimal getAccountBalance(Long accountId) {
        // Implementa la lógica para obtener el saldo de la cuenta
        return new BigDecimal("1000.00"); // Placeholder
    }

    /**
     * Actualiza el saldo de una cuenta.
     * @param accountId El ID de la cuenta.
     * @param newBalance El nuevo saldo de la cuenta.
     */
    private void updateAccountBalance(Long accountId, BigDecimal newBalance) {
        // Implementa la lógica para actualizar el saldo de la cuenta
    }



    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(Long accountId) {
        return transactionFeignClient.getTransactionsByAccountId(accountId);
    }

    /**
     * Get transaction details by transaction ID.
     * @param transactionId The ID of the transaction.
     * @return ResponseEntity with the transaction details.
     */
    public ResponseEntity<TransactionDTO> getTransactionById(Long transactionId) {
        return transactionFeignClient.getTransactionById(transactionId);
    }
}
