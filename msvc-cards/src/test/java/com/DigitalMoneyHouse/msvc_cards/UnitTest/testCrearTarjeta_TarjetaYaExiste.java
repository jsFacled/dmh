@Test
public void testCrearTarjeta_TarjetaYaExiste() {
        // Dado
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(1L);
        when(cardFeignClient.createCard(any(CardCreationDTO.class)))
        .thenThrow(new CardAlreadyExistsException("La tarjeta ya existe."));

        // Cuando y Entonces
        CardAlreadyExistsException ex = assertThrows(CardAlreadyExistsException.class, () -> {
        cardsService.createCard(accountId, cardRequest);
        });
        assertEquals("La tarjeta ya existe.", ex.getMessage());
        }
