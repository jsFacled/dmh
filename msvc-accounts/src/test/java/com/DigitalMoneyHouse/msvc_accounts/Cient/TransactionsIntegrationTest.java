package com.DigitalMoneyHouse.msvc_accounts.Cient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.http.HttpHeaders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetTransactionById_success() {
        // Dado: una transacción válida con token válido
        HttpHeaders headers = new Http;
        headers.set("Authorization", "Bearer validToken");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Cuando: se llama al endpoint de transacción
        ResponseEntity<TransactionDTO> response = restTemplate.exchange(
                "/accounts/1/activity/123", HttpMethod.GET, entity, TransactionDTO.class);

        // Entonces: el resultado debe ser el detalle de la transacción
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetTransactionById_tokenInvalid() {
        // Dado: un token inválido
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer invalidToken");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Cuando: se llama al endpoint con un token inválido
        ResponseEntity<String> response = restTemplate.exchange(
                "/accounts/1/activity/123", HttpMethod.GET, entity, String.class);

        // Entonces: el resultado debe ser un estado 403
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testGetTransactionById_transactionNotFound() {
        // Dado: una transacción inexistente
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer validToken");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Cuando: se llama al endpoint con una transacción no existente
        ResponseEntity<String> response = restTemplate.exchange(
                "/accounts/1/activity/999", HttpMethod.GET, entity, String.class);

        // Entonces: el resultado debe ser un estado 404
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetTransactionById_accountNotFound() {
        // Dado: una cuenta inexistente
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer validToken");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Cuando: se llama al endpoint con una cuenta no existente
        ResponseEntity<String> response = restTemplate.exchange(
                "/accounts/999/activity/123", HttpMethod.GET, entity, String.class);

        // Entonces: el resultado debe ser un estado 400
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
