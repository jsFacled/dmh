


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CardsClientServiceTest {

    @Mock
    private ICardFeignClient cardFeignClient;

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private CardsClientService cardsService;

    @Test
    public void testCreateCard_Success() {
        // Given
        Long accountId = 1L;
        Long userId = 10L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        // Configuración del DTO
        cardRequest.setNumber("1234567890123456");
        cardRequest.setHolderName("John Doe");
        cardRequest.setExpiration("12/25");
        cardRequest.setCvc("123");
        cardRequest.setCardType(CardType.DEBIT);
        cardRequest.setBalance(BigDecimal.valueOf(1000));

        CardCreationDTO cardCreationDTO = CardsClientService.buildCardCreationDTO(accountId, cardRequest, userId);
        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(userId);
        when(cardFeignClient.createCard(cardCreationDTO)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(null));

        // When
        ResponseEntity<?> response = cardsService.createCard(accountId, cardRequest);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cardFeignClient, times(1)).createCard(any(CardCreationDTO.class));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testCreateCard_AccountNotFoundException() {
        // Given
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();

        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(null);

        // When
        cardsService.createCard(accountId, cardRequest);
    }

    @Test(expected = CardAlreadyExistsException.class)
    public void testCreateCard_CardAlreadyExistsException() {
        // Given
        Long accountId = 1L;
        Long userId = 10L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(userId);

        CardCreationDTO cardCreationDTO = CardsClientService.buildCardCreationDTO(accountId, cardRequest, userId);
        when(cardFeignClient.createCard(cardCreationDTO)).thenThrow(CardAlreadyExistsException.class);

        // When
        cardsService.createCard(accountId, cardRequest);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateCard_GeneralException() {
        // Given
        Long accountId = 1L;
        Long userId = 10L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(userId);

        CardCreationDTO cardCreationDTO = CardsClientService.buildCardCreationDTO(accountId, cardRequest, userId);
        when(cardFeignClient.createCard(cardCreationDTO)).thenThrow(RuntimeException.class);

        // When
        cardsService.createCard(accountId, cardRequest);
    }
}
