package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;


@SpringBootTest
@AutoConfigureMockMvc
public class UnitAuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void login_WithValidCredentials_ReturnsToken() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password");
        when(authService.authenticateAndGenerateToken(any(LoginRequestDTO.class))).thenReturn("mockedToken");

        // Act & Assert
        mockMvc.perform((RequestBuilder) post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.valueOf("{\"email\": \"test@example.com\", \"password\": \"password\"}")))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content());
    }

    @Test
    void login_WithInvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        when(authService.authenticateAndGenerateToken(any(LoginRequestDTO.class)))
                .thenThrow(new RuntimeException("Invalid Credentials"));

        // Act & Assert
        mockMvc.perform((RequestBuilder) MockServerHttpRequest.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.valueOf("{\"email\": \"test@example.com\", \"password\": \"wrongPassword\"}")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid credentials")));
    }

    @Test
    void logout_WithValidToken_ReturnsOk() throws Exception {
        // Arrange
        when(authService.validateToken(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform((RequestBuilder) post("/auth/logout")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));
    }
}
