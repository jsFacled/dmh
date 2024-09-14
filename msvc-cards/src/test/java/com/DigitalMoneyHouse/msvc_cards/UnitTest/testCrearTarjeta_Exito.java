@Test
public void testCrearTarjeta_Exito() {
        // Dado
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        // Configura los datos de prueba para cardRequest

        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(1L);
        when(cardFeignClient.createCard(any(CardCreationDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // Cuando
        ResponseEntity<?> response = cardsService.createCard(accountId, cardRequest);

        // Entonces
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cardFeignClient, times(1)).createCard(any(CardCreationDTO.class));
        }
