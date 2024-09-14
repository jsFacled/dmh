



@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CardsClientServiceTest {

    @Mock
    private ICardFeignClient cardFeignClient;

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private CardsClientService cardsService;

    @Test
    void borrarTarjeta_tarjetaExistente() {
        // Mock del FeignClient
        Mockito.doNothing().when(cardFeignClient).deleteCard(1L);

        ResponseEntity<Void> response = cardsService.borrarTarjeta(1L);

        // Verificar que el método del FeignClient fue llamado
        Mockito.verify(cardFeignClient, Mockito.times(1)).deleteCard(1L);

        // Verificar el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void borrarTarjeta_tarjetaNoExistente() {
        // Simula una excepción cuando la tarjeta no existe
        Mockito.doThrow(new RuntimeException("Tarjeta no encontrada")).when(cardFeignClient).deleteCard(999L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardsService.borrarTarjeta(999L);
        });

        assertEquals("Tarjeta no encontrada", exception.getMessage());
    }

    @Test
    void borrarTarjeta_errorEnFeignClient() {
        // Simula un error en el FeignClient
        Mockito.doThrow(new RuntimeException("Error en el servicio")).when(cardFeignClient).deleteCard(3L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardsService.borrarTarjeta(3L);
        });

        assertEquals("Error en el servicio", exception.getMessage());
    }
}
