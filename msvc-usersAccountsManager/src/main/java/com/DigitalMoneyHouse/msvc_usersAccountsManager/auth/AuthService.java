package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.JwtUtil;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserClient userClient;
    private final JwtUtil jwtUtil;

    public AuthService(IUserClient userClient, JwtUtil jwtUtil) {
        this.userClient = userClient;
        this.jwtUtil = jwtUtil;
    }


    public String authenticateAndGenerateToken(LoginRequestDTO loginRequest) {
        try {
            System.out.println("Enviando solicitud de validación a ms-users con loginRequest: " + loginRequest);
            ResponseEntity<String> response = userClient.validateUser(loginRequest);
            System.out.println("Respuesta recibida de ms-users: " + response);

            if (response.getStatusCode() == HttpStatus.OK) {
                String jwt = jwtUtil.generateToken(loginRequest.getEmail());
                System.out.println("El token generado es :" + jwt);
                return jwt;
            } else {
                throw new RuntimeException("Validación fallida: " + response.getBody());
            }
        } catch (Exception e) {
            System.out.println("Error al comunicarse con ms-users: " + e.getMessage());
            throw new RuntimeException("Error interno del servidor", e);
        }
    }


    // Método para validar un token JWT
    public boolean validateToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                // Validar el token utilizando JwtUtil
                return jwtUtil.validateToken(token);
            } catch (Exception e) {
                System.out.println("Error al validar el token: " + e.getMessage());
                return false;
            }
        } else {
            // Si no se proporciona un token válido
            System.out.println("Encabezado Authorization faltante o inválido");
            return false;
        }
    }
}

