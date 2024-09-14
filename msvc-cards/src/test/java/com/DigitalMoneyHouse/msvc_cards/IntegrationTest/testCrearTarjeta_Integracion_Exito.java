@Test
@WithMockUser
public void testCrearTarjeta_Integracion_Exito() throws Exception {
        Long accountId = 1L;
        CardRequestDTO cardRequest = new CardRequestDTO();
        // Configura datos de prueba en cardRequest

        mockMvc.perform(post("/accounts/{accountId}/cards", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(cardRequest)))
        .andExpect(status().isCreated());
        }
