package uz.dev.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.dev.dto.Hour;
import uz.dev.model.User;
import uz.dev.process.ProcessCallbackQuery;
import uz.dev.process.ProcessMessage;
import uz.dev.utils.Config;
import uz.dev.utils.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class BotService extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = Config.BOT_USERNAME;
    private static final String BOT_TOKEN = Config.BOT_TOKEN;

    private static final String API_TOKEN = Config.API_TOKEN;
    private static final String API_KEY = Config.API_KEY;

    private Map<String, User> users = new HashMap<>();

    private static final ProcessMessage processMessage = ProcessMessage.getInstance();
    private static final InlineButtonService inlineButtonService = InlineButtonService.getInstance();
    private static final ProcessCallbackQuery processCallbackQuery = ProcessCallbackQuery.getInstance();

    public static BotService instance;

    public static BotService getInstance() {
        if (instance == null) {
            synchronized (BotService.class) {
                if (instance == null) {
                    instance = new BotService();
                }
            }
        }
        return instance;
    }

    public BotService() {
        super(BOT_TOKEN);
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {

            User user = checkedUser(update.getMessage());

            processMessage.process(update.getMessage(), user);

        } else if (update.hasCallbackQuery()) {

            User user = getUser(update.getCallbackQuery().getFrom().getId());

            processCallbackQuery.process(update.getCallbackQuery(), user);

        }

    }

    private User checkedUser(Message message) {

        List<String> list = new ArrayList<>();

        list.add("Tashkent");

        User user = users.putIfAbsent(message.getChatId().toString(), new User(
                message.getChatId(), message.getChat().getFirstName(), message.getChat().getLastName(),
                message.getChat().getUserName(), list, "", Step.START, 0
        ));

        if (user == null) {

            user = users.get(message.getChatId().toString());

        }

        return user;

    }

    private User getUser(Long chatId) {

        User user = users.get(chatId.toString());

        if (user == null) {
            return new User();
        }

        return user;

    }

    public void sendMessage(Long chatId, String message, ReplyKeyboard replyKeyboard) {

        try {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(chatId);

            sendMessage.setText(message);

            sendMessage.setParseMode("HTML");

            sendMessage.setReplyMarkup(replyKeyboard);


            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMessage(Long chatId, String message) {

        try {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(chatId);

            sendMessage.setText(message);

            sendMessage.setParseMode("HTML");

            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendAnswerMessage(String chatId, String text) {

        try {

            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();

            answerCallbackQuery.setCallbackQueryId(chatId);

            answerCallbackQuery.setText(text);

            execute(answerCallbackQuery);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteMessage(Long chatId, Integer messageId) {

        try {

            DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), messageId);

            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void editMessage(Long chatId, Integer messageId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {

        try {

            EditMessageText editMessageText = new EditMessageText();

            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(text);

            editMessageText.setParseMode("HTML");

            editMessageText.setReplyMarkup(inlineKeyboardMarkup);

            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void editMessageForPage(Long chatId, Integer messageId, int currentPage, User user) {

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);

        List<Hour> hours = processMessage.getHours(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtonService.buildButtonsForHours(user, currentPage, hours);

        String madeClockWeatherText = processMessage.makeClockWeatherText(hours, currentPage, user);

        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        editMessageText.setParseMode("HTML");

        editMessageText.setText(madeClockWeatherText);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


    public String getApiUrl(String apiName, String city, String language) {

        return API_TOKEN + apiName + "?q=" + city + "&lang=" + language;

    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

}
