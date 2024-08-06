package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.jwt;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Este servicio se encarga de manejar la invalidación y la limpieza de los tokens
 * y se integra en la lógica del JwtUtil, el JwtAuthenticationFilter y el AuthController.
 *
 *  */
@Service
public class TokenBlacklistService  {

    private final ConcurrentHashMap<String, Long> invalidatedTokens = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler;

    @PostConstruct
    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::cleanExpiredTokens, 0, 1, TimeUnit.HOURS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

    public void invalidateToken(String token, long expiryTime) {
        invalidatedTokens.put(token, expiryTime);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.containsKey(token);
    }

    private void cleanExpiredTokens() {
        long now = System.currentTimeMillis();
        invalidatedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}