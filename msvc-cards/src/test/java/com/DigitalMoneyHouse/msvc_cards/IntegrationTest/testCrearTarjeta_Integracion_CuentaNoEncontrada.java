@Test
@WithMockUser
public void testCrearTarjeta_Integracion_CuentaNoEncontrada() throws Exception {
        Long accountId = 99L; // Cuenta inexistente
        CardRequestDTO cardRequest = new CardRequestDTO();

        mockMvc.perform(post("/accounts/{accountId}/cards", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(cardRequest)))
        .andExpect(status().isNotFound());
        }
