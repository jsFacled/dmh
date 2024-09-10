package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenBlacklistService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //Se obtiene el token.
        String token = jwtUtil.extractToken(request);

        //Validación del token agregando credenciales de Autenticacion.
        if (token != null && jwtUtil.validateToken(token) && !tokenService.isTokenInvalidated(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //Pasa la Solicitud y la Respuesta al Siguiente Filtro en la cadena, independientemente de si el token fue válido o no.
        filterChain.doFilter(request, response);
    }

}
