@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CardsClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardsClientService cardsService;

    @Test
    public void testCrearTarjeta_Success() throws Exception {
        // Given
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        cardRequest.setNumber("1234567890123456");
        cardRequest.setHolderName("John Doe");
        cardRequest.setExpiration("12/25");
        cardRequest.setCvc("123");
        cardRequest.setCardType(CardType.DEBIT);
        cardRequest.setBalance(BigDecimal.valueOf(1000));

        when(cardsService.createCard(eq(accountId), any(CardRequestDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(null));

        // When
        mockMvc.perform(post("/accounts/{accountId}/cards", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cardRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCrearTarjeta_CardAlreadyExists() throws Exception {
        // Given
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();

        when(cardsService.createCard(eq(accountId), any(CardRequestDTO.class)))
                .thenThrow(CardAlreadyExistsException.class);

        // When
        mockMvc.perform(post("/accounts/{accountId}/cards", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cardRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCrearTarjeta_AccountNotFound() throws Exception {
        // Given
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();

        when(cardsService.createCard(eq(accountId), any(CardRequestDTO.class)))
                .thenThrow(AccountNotFoundException.class);

        // When
        mockMvc.perform(post("/accounts/{accountId}/cards", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(cardRequest)))
                .andExpect(status().isNotFound());
    }
}
