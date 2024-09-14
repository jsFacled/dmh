package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 Prueba de Integración para el flujo de Login y Logout
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_AndLogoutFlow() throws Exception {
        // Test login
        mockMvc.perform((RequestBuilder) post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.valueOf("{\"email\": \"test@example.com\", \"password\": \"password\"}")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());

        // Simulate token from response (you would typically extract the token here)
        String validToken = "Bearer mockedToken";

        // Test logout
        mockMvc.perform((RequestBuilder) post("/auth/logout")
                        .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));
    }
}
