package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.JwtUtil;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/*
*
* Pruebas unitarias
*
* */
@SpringBootTest
public class UnitAuthServiceTest {

    @Mock
    private IUserClient userClient;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticateAndGenerateToken_WithValidCredentials_ReturnsToken() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password");
        when(userClient.validateUser(loginRequest)).thenReturn(new ResponseEntity<>("User Valid", HttpStatus.OK));
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mockedToken");

        // Act
        String token = authService.authenticateAndGenerateToken(loginRequest);

        // Assert
        assertEquals("mockedToken", token);
        verify(userClient).validateUser(loginRequest);
        verify(jwtUtil).generateToken("test@example.com");
    }

    @Test
    void authenticateAndGenerateToken_WithInvalidCredentials_ThrowsException() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "wrongPassword");
        when(userClient.validateUser(loginRequest)).thenReturn(new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.authenticateAndGenerateToken(loginRequest));
        verify(userClient).validateUser(loginRequest);
    }
}
}
