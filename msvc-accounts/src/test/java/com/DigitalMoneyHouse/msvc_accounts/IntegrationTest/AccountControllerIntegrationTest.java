package com.DigitalMoneyHouse.msvc_accounts.IntegrationTest;
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testGetAccountBalance_Success() throws Exception {
        Long accountId = 1L;
        BigDecimal expectedBalance = new BigDecimal("100.00");

        Mockito.when(accountService.getAccountBalance(accountId)).thenReturn(expectedBalance);

        mockMvc.perform(get("/accounts/{id}/balance", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

    @Test
    public void testGetAccountBalance_AccountNotFound() throws Exception {
        Long accountId = 1L;

        Mockito.when(accountService.getAccountBalance(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/accounts/{id}/balance", accountId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAccount_Success() throws Exception {
        Long userId = 1L;
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setCvu("1234567890");
        accountResponseDTO.setAlias("alias.test");

        Mockito.when(accountService.createAccount(userId)).thenReturn(accountResponseDTO);

        mockMvc.perform(post("/accounts/create/{userId}", userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cvu").value("1234567890"))
                .andExpect(jsonPath("$.alias").value("alias.test"));
    }

    @Test
    public void testPatchAccount_Success() throws Exception {
        Long accountId = 1L;
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("200.00"));
        accountDTO.setCvu("1234567890");
        accountDTO.setAlias("alias.updated");

        Map<String, Object> updates = new HashMap<>();
        updates.put("balance", "200.00");

        Mockito.when(accountService.patchAccount(accountId, updates)).thenReturn(accountDTO);

        mockMvc.perform(patch("/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":\"200.00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("200.00"))
                .andExpect(jsonPath("$.alias").value("alias.updated"));
    }

    @Test
    public void testPatchAccount_AccountNotFound() throws Exception {
        Long accountId = 1L;

        Mockito.when(accountService.patchAccount(Mockito.eq(accountId), Mockito.anyMap())).thenThrow(new EntityNotFoundException("Account not found"));

        mockMvc.perform(patch("/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":\"200.00\"}"))
                .andExpect(status().isNotFound());
    }
}
