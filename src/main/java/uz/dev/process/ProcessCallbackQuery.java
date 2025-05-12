package uz.dev.process;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import uz.dev.model.User;
import uz.dev.service.BotService;
import uz.dev.service.InlineButtonService;
import uz.dev.service.LanguageService;
import uz.dev.service.ReplyKeyboardService;
import uz.dev.utils.Const;
import uz.dev.utils.Step;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ProcessCallbackQuery {

    public static ProcessCallbackQuery instance;

    private static final BotService botService = BotService.getInstance();
    private static final ReplyKeyboardService replyKeyboardService = ReplyKeyboardService.getInstance();
    private static final ProcessMessage processMessage = ProcessMessage.getInstance();
    private static final InlineButtonService inlineButtonService = InlineButtonService.getInstance();

    private final Map<String, User> users = BotService.getInstance().getUsers();

    public static ProcessCallbackQuery getInstance() {
        if (instance == null) {
            synchronized (ProcessCallbackQuery.class) {
                if (instance == null) {
                    instance = new ProcessCallbackQuery();
                }
            }
        }
        return instance;
    }

    public void process(CallbackQuery callbackQuery, User user) {

        String data = callbackQuery.getData();

        Long chatId = callbackQuery.getFrom().getId();

        if (user.getStep().equals(Step.SELECT_LANG)) {

            String sendText = "";

            switch (data) {

                case "lang-uz" -> {
                    sendText = "Siz O‘zbek tilini tanladingiz! 🇺🇿";
                    user.setLanguage("uz");
                }

                case "lang-eng" -> {
                    sendText = "You selected English! 🇬🇧";
                    user.setLanguage("en");
                }

                case "lang-ru" -> {
                    sendText = "Вы выбрали русский язык! 🇷🇺";
                    user.setLanguage("ru");
                }

            }

            user.setStep(Step.SELECT_MENU);

            users.put(chatId.toString(), user);

            botService.setUsers(users);

            ReplyKeyboardMarkup replyKeyboardMarkup = replyKeyboardService.buildButtonsForMenu(user);

            botService.sendMessage(chatId, sendText, replyKeyboardMarkup);

            botService.deleteMessage(chatId, callbackQuery.getMessage().getMessageId());

        } else if (user.getStep().equals(Step.SELECT_MENU)) {

            String sendText = "";

            switch (data) {

                case "update" -> {

                    LocalTime time = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    formatter.format(time);

                    String text = "Yangilangan vaqti : " + time.format(formatter);

                    botService.sendAnswerMessage(callbackQuery.getId(), text);

                    String string = processMessage.sendCurrentWeather(user);

                    Integer messageId = callbackQuery.getMessage().getMessageId();

                    InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtonService.buildButtonsForCurrentWeather(user);

                    botService.editMessage(chatId, messageId, string, inlineKeyboardMarkup);

                }
                case "edit" -> {

                    String sendMessage = LanguageService.getText(user.getLanguage(), "inf_btn_edit");

                    Integer messageId = callbackQuery.getMessage().getMessageId();

                    ReplyKeyboardMarkup buildCancelButton = replyKeyboardService.buildCancelButton(user);

                    user.setStep(Step.ENTER_NEW_CITY);
                    users.put(messageId.toString(), user);
                    botService.setUsers(users);

                    botService.sendMessage(chatId, sendMessage, buildCancelButton);

                    botService.deleteMessage(chatId, messageId);

                }

                case Const.NEXT_BUTTON_PREFIX -> {

                    changePage(chatId, callbackQuery.getMessage().getMessageId(), 1, user);

                }
                case Const.PREV_BUTTON_PREFIX -> {

                    changePage(chatId, callbackQuery.getMessage().getMessageId(), -1, user);

                }
                case Const.DELETE_BUTTON_PREFIX -> {

                    botService.deleteMessage(chatId, callbackQuery.getMessage().getMessageId());

                }

            }

        }

    }

    private void changePage(Long chatId, Integer messageId, int page, User user) {

        int nextPage = user.getCurrentPage() + page;

        user.setCurrentPage(nextPage);

        users.put(chatId.toString(), user);

        botService.setUsers(users);

        botService.editMessageForPage(chatId, messageId, nextPage, user);

    }
}
