

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CardsClientControllerIntegrationBorrarTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ICardFeignClient cardFeignClient;

    @Test
    void testEliminarTarjeta_Exitoso() {
        // Se espera que la tarjeta exista y sea eliminada
        ResponseEntity<Void> response = restTemplate.exchange("/accounts/1/cards/1", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testEliminarTarjeta_TarjetaNoEncontrada() {
        // Se espera un 404 cuando la tarjeta no existe
        ResponseEntity<Void> response = restTemplate.exchange("/accounts/1/cards/999", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testEliminarTarjeta_ErrorServidor() {
        // Simular un error en ms-cards que lanza una excepción
        Mockito.doThrow(new RuntimeException("Error en el servicio")).when(cardFeignClient).deleteCard(3L);

        ResponseEntity<Void> response = restTemplate.exchange("/accounts/1/cards/3", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
