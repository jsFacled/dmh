@Test
public void testCrearTarjeta_CuentaNoEncontrada() {
        // Dado
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        when(accountRepository.findUserIdByAccountId(accountId)).thenReturn(null);

        // Cuando y Entonces
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> {
        cardsService.createCard(accountId, cardRequest);
        });
        assertEquals("El accountId proporcionado no corresponde a un usuario válido.", ex.getMessage());
        }
