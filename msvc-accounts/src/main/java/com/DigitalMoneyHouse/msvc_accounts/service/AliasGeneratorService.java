package com.DigitalMoneyHouse.msvc_accounts.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AliasGeneratorService {

    private List<String> words;
    private Random random = new Random();
    public AliasGeneratorService() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find words file");
            }
            words = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read words file", e);
        }
    }

   /*
   *Con readAllLines lee desde la app corriendo en local, no sirve para docker que utiliza el .jar.
   *
    public AliasGeneratorService() {
        try {
            words = Files.readAllLines(Paths.get("src/main/resources/static/words.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Could not read words file", e);
        }
    }
*/
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