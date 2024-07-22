package com.DigitalMoneyHouse.msvc_usersAccountsManager.Config;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
*/
//@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/manager/register").permitAll() // Permitir acceso sin autenticación
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // Configuración de autenticación básica

        return http.build();
    }

     */
}

