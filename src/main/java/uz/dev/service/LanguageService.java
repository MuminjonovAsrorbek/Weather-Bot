package uz.dev.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LanguageService {
    private static JsonNode translations;

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            translations = objectMapper.readTree(new File("languages.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getText(String lang, String key) {
        return translations.has(lang) && translations.get(lang).has(key)
                ? translations.get(lang).get(key).asText()
                : "Noma’lum kalit!";
    }

}
