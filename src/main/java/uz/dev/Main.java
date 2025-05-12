package uz.dev;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.dev.service.BotService;

public class Main {

    public static void main(String[] args) {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            BotService myBot = BotService.getInstance();

            telegramBotsApi.registerBot(myBot);

            System.out.println("Bot started");

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}