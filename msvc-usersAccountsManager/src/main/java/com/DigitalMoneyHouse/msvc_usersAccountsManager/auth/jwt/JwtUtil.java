package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;

import java.util.Date;

/** Clase utilitaria para manejar la generación y validación del JWT:
 *
 *
 *
 */


@Component
public class JwtUtil {
    private final byte[] SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();


    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenBlacklistService tokenService;


    public String generateToken(String email) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 180000)) //3 minutos
                //.setExpiration(new Date(currentTimeMillis + 1000 * 60 * 60)) // 1 hora de expiración
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public Date extractExpiration(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }


    //Verifica el username, la expiración y si ha sido invalidado en el logout.
    public boolean validateToken(String token) {
        final String username = extractUsername(token);
        return (username != null && !isTokenExpired(token) && !tokenService.isTokenInvalidated(token));
    }

    /*
    public boolean validateToken(String token) {
        // Extraer el nombre de usuario del token
        final String username = extractUsername(token);
        System.out.println("Extracted Username: " + username);

        boolean isExpired = false;
        boolean isInvalidated = false;

        try {
            // Verificar si el token ha expirado
            isExpired = isTokenExpired(token);
            System.out.println("Token Expiration Status: " + isExpired);

            // Verificar si el token ha sido invalidado
            isInvalidated = tokenService.isTokenInvalidated(token);
            System.out.println("Token Invalidated Status: " + isInvalidated);

        } catch (Exception e) {
            // Imprimir el error si ocurre una excepción
            System.out.println("Error while validating token: " + token);
            e.printStackTrace(); // Imprimir la traza completa de la excepción
        }

        // Imprimir el resultado final de la validación del token
        boolean isValid = (username != null && !isExpired && !isInvalidated);
        if (isValid) {
            System.out.println("Token is valid.");
        } else {
            System.out.println("Token is invalid. Username: " + username +
                    ", Expired: " + isExpired +
                    ", Invalidated: " + isInvalidated);
        }

        return isValid;
    }


 */
    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // Método para obtener obtener datos del usuario y crear el objeto Authentication
    public Authentication getAuthentication(String token) {
        // Extrae el nombre de usuario del token JWT
        String username = extractUsername(token);

        // Carga los detalles del usuario desde el UserDetailsService usando el nombre de usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Crea y devuelve un objeto Authentication que contiene los detalles del usuario y sus autoridades
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}