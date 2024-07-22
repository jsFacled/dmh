package com.DigitalMoneyHouse.msvc_accounts.service;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CVUGeneratorService {

    private static final String CVU_PREFIX = "0000000000000000000000"; // CVU debe ser de 22 dígitos

    public String generateCVU() {
        SecureRandom random = new SecureRandom();
        StringBuilder cvu = new StringBuilder(CVU_PREFIX);

        // Generar los últimos 22 dígitos aleatoriamente
        for (int i = 0; i < CVU_PREFIX.length(); i++) {
            cvu.setCharAt(i, (char) ('0' + random.nextInt(10)));
        }

        return cvu.toString();
    }



}
