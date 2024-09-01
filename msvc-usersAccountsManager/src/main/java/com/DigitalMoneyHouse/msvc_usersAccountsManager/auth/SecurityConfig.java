package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.JwtAuthenticationFilter;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.JwtUtil;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt.TokenBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenService;

    public SecurityConfig(JwtUtil jwtUtil, TokenBlacklistService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers(
                                        "auth/login",
                                        "auth/logout",
                                        "manager/hello",
                                        "manager/register",
                                        "swagger-ui/**",
                                        "v3/api-docs",
                                        "actuator/**",

                                        "http://localhost:8082/users").permitAll()

                                .anyRequest().authenticated()
                )

                .csrf().disable()
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Añade el filtro JWT antes del filtro de autenticación por defecto
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, tokenService);
    }


}
