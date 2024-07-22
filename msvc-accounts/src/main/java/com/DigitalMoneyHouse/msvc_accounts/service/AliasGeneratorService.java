package com.DigitalMoneyHouse.msvc_accounts.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@Service
public class AliasGeneratorService {

    private List<String> words;
    private Random random = new Random();

    public AliasGeneratorService() {
        try {
            words = Files.readAllLines(Paths.get("src/main/resources/static/words.txt"));
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