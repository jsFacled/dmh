package com.DigitalMoneyHouse.msvc_transactions;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetActivity_success() {
        // Dado: una cuenta válida con token válido
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer validToken");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Cuando: se llama al endpoint de actividad
        ResponseEntity<List<TransactionDTO>> response = restTemplate.exchange(
                "/accounts/1/activity", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

        // Entonces: el resultado debe ser una lista de transacciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetActivity_tokenInvalid() {
        // Dado: un token inválido
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer invalidToken");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Cuando: se llama al endpoint con un token inválido
        ResponseEntity<String> response = restTemplate.exchange(
                "/accounts/1/activity", HttpMethod.GET, entity, String.class);

        // Entonces: el resultado debe ser un estado 403
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
