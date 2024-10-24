package com.DigitalMoneyHouse.msvc_accounts.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AliasGeneratorService {

    private List<String> words;
    private Random random = new Random();


    /*
     * Esta forma en Docker no funciona porque al leer words.txt utiliza otra ruta 
     */
    /*
    public AliasGeneratorService() {
        try {
            words = Files.readAllLines(Paths.get("src/main/resources/static/words.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Could not read words file", e);
        }
    }
*/

//Forma sugerida para Dockerizar
public AliasGeneratorService() {
        try (InputStream inputStream = getClass().getResourceAsStream("/static/words.txt")) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find words file");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                words = reader.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read words file", e);
        }
    }

    public String generateAlias() {
        if (words == null || words.size() < 3) {
            throw new IllegalStateException("Not enough words to generate alias");
        }

        String word1 = getRandomWord();
        String word2 = getRandomWord();
        String word3 = getRandomWord();

        return String.format("%s.%s.%s", word1, word2, word3);
    }

    private String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }
}