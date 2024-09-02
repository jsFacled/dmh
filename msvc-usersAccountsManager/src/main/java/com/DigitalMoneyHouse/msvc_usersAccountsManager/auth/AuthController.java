package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.JwtUtil;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.TokenBlacklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {


    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;

        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/hello")
    public String index2() {
        return "  *  *  *  *  *  Helloooo desde auth en ms-uaM. Tu jwt fue autenticado con éxito ! ! ! *  *  *";
    }


    @CrossOrigin("http://localhost:8080")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String token = authService.authenticateAndGenerateToken(loginRequest);
            System.out.println("* * L38 * * Iniciando dentro de AuthController la solicitud al AuthService para que autentique");
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" * * L41 * * Invalid credentials desde linea 28 en metodo login del AuthController. Pero el context holder es: "+ SecurityContextHolder.getContext());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7); // Eliminar "Bearer " del token
            long expiryTime = jwtUtil.extractExpiration(jwtToken).getTime();
            tokenBlacklistService.invalidateToken(jwtToken, expiryTime);
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout");
        }
    }

}
