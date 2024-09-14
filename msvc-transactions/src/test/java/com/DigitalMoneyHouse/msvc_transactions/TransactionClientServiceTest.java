package com.DigitalMoneyHouse.msvc_transactions;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionClientServiceTest {

    @MockBean
    private ITransactionFeignClient transactionFeignClient;

    @MockBean
    private IAccountRepository accountRepository;

    @InjectMocks
    private TransactionClientService transactionClientService;

    @Test
    public void testGetTransactionsByAccountId_success() {
        // Dado: una cuenta válida con transacciones
        Long accountId = 1L;
        List<TransactionDTO> mockTransactions = Arrays.asList(
                new TransactionDTO(1L, LocalDateTime.now(), accountId, TransactionType.TRANSFER_BETWEEN_ACCOUNTS, ProductOriginType.ACCOUNT, 1L, 2L, BigDecimal.TEN, "Transferencia")
        );
        when(transactionFeignClient.getTransactionsByAccountId(accountId))
                .thenReturn(ResponseEntity.ok(mockTransactions));

        // Cuando: se llama al método getTransactionsByAccountId
        ResponseEntity<List<TransactionDTO>> response = transactionClientService.getTransactionsByAccountId(accountId);

        // Entonces: el resultado debe ser correcto
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetTransactionsByAccountId_accountNotFound() {
        // Dado: una cuenta inexistente
        Long accountId = 999L;
        when(accountRepository.existsById(accountId)).thenReturn(false);

        // Cuando: se intenta obtener transacciones
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionClientService.getTransactionsByAccountId(accountId);
        });

        // Entonces: se lanza una excepción con el estado 400
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Cuenta no encontrada", exception.getReason());
    }

    @Test
    public void testExecuteTransfer_insufficientFunds() {
        // Dado: una cuenta con fondos insuficientes
        Long accountId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO(1L, LocalDateTime.now(), accountId, TransactionType.TRANSFER_BETWEEN_ACCOUNTS, ProductOriginType.ACCOUNT, 1L, 2L, BigDecimal.valueOf(100), "Transferencia");
        when(accountRepository.decreaseBalanceIfSufficient(accountId, transactionDTO.getAmount())).thenReturn(0);

        // Cuando: se intenta ejecutar la transferencia
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionClientService.executeTransfer(transactionDTO);
        });

        // Entonces: se lanza una excepción con el estado 400
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Fondos insuficientes en la cuenta de origen", exception.getReason());
    }
}
