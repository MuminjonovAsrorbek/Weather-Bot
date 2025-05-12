package uz.dev.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static final String BOT_USERNAME = dotenv.get("BOT_USERNAME");
    public static final String BOT_TOKEN = dotenv.get("BOT_TOKEN");
    public static final String API_TOKEN = dotenv.get("API_TOKEN");
    public static final String API_KEY = dotenv.get("API_KEY");

}
