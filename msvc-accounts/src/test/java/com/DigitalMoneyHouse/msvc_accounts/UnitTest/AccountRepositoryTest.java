package com.DigitalMoneyHouse.msvc_accounts.UnitTest;
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private IAccountRepository accountRepository;

    @Test
    public void testFindBalanceByAccountId() {
        Account account = new Account();
        account.setBalance(new BigDecimal("150.00"));
        account = accountRepository.save(account);

        BigDecimal balance = accountRepository.findBalanceByAccountId(account.getId());
        assertNotNull(balance);
        assertEquals(new BigDecimal("150.00"), balance);
    }

    @Test
    public void testDecreaseBalanceIfSufficient() {
        Account account = new Account();
        account.setBalance(new BigDecimal("200.00"));
        account = accountRepository.save(account);

        int updated = accountRepository.decreaseBalanceIfSufficient(account.getId(), new BigDecimal("50.00"));
        assertEquals(1, updated);

        Account updatedAccount = accountRepository.findById(account.getId()).get();
        assertEquals(new BigDecimal("150.00"), updatedAccount.getBalance());
    }
}
