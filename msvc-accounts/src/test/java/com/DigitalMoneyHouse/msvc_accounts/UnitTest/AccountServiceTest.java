package com.DigitalMoneyHouse.msvc_accounts.UnitTest;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AliasGeneratorService aliasGeneratorService;

    @Mock
    private CVUGeneratorService cvuGeneratorService;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testGetAccountBalance_ValidAccount() {
        // Configuración del mock
        Long accountId = 1L;
        BigDecimal expectedBalance = new BigDecimal("100.00");
        Mockito.when(accountRepository.findBalanceByAccountId(accountId)).thenReturn(expectedBalance);

        // Llamada al método a probar
        BigDecimal actualBalance = accountService.getAccountBalance(accountId);

        // Verificación
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void testGetAccountBalance_AccountNotFound() {
        Long accountId = 1L;
        Mockito.when(accountRepository.findBalanceByAccountId(accountId)).thenReturn(null);

        // Verificación de excepción
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountBalance(accountId);
        });
    }

    @Test
    public void testCreateAccount_Success() {
        Long userId = 1L;
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setCvu("1234567890");
        account.setAlias("alias.cvu");

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        Mockito.when(cvuGeneratorService.generateCVU()).thenReturn("1234567890");
        Mockito.when(aliasGeneratorService.generateAlias()).thenReturn("alias.cvu");

        AccountResponseDTO response = accountService.createAccount(userId);

        assertNotNull(response);
        assertEquals("1234567890", response.getCvu());
        assertEquals("alias.cvu", response.getAlias());
    }

    @Test
    public void testPatchAccount_ValidAccount() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("100.00"));

        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", new BigDecimal("200.00"));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        AccountDTO updatedAccountDTO = accountService.patchAccount(accountId, updates);

        assertNotNull(updatedAccountDTO);
        assertEquals(new BigDecimal("200.00"), updatedAccountDTO.getBalance());
    }

    @Test
    public void testPatchAccount_AccountNotFound() {
        Long accountId = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", new BigDecimal("200.00"));

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            accountService.patchAccount(accountId, updates);
        });
    }
}
