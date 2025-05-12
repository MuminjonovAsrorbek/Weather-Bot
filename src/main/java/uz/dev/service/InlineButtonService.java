package uz.dev.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.dev.dto.Hour;
import uz.dev.model.User;
import uz.dev.process.ProcessMessage;
import uz.dev.utils.Const;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InlineButtonService {

    public static InlineButtonService instance;

    public static InlineButtonService getInstance() {
        if (instance == null) {
            synchronized (InlineButtonService.class) {
                if (instance == null) {
                    instance = new InlineButtonService();
                }
            }
        }
        return instance;
    }

    public InlineKeyboardMarkup buildInlineButtonForSelectLanguages() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton uzButton = new InlineKeyboardButton();

        uzButton.setText(" O‘zbekcha \uD83C\uDDFA\uD83C\uDDFF ");
        uzButton.setCallbackData("lang-uz");

        InlineKeyboardButton engButton = new InlineKeyboardButton();

        engButton.setText("English \uD83C\uDDEC\uD83C\uDDE7");
        engButton.setCallbackData("lang-eng");

        InlineKeyboardButton ruButton = new InlineKeyboardButton();

        ruButton.setText("Русский \uD83C\uDDF7\uD83C\uDDFA");
        ruButton.setCallbackData("lang-ru");

        rows.add(List.of(uzButton, engButton, ruButton));

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;

    }

    public InlineKeyboardMarkup buildButtonsForCurrentWeather(User user) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        InlineKeyboardButton updateButton = new InlineKeyboardButton();

        String language = user.getLanguage();

        updateButton.setText(LanguageService.getText(language, "in_btn_update"));

        updateButton.setCallbackData("update");

        firstRow.add(updateButton);

        InlineKeyboardButton shareButton = new InlineKeyboardButton();

        shareButton.setText(LanguageService.getText(language, "in_btn_share"));

        Long userId = user.getUserId();

        String text = ProcessMessage.getInstance()
                .sendCurrentWeather(user).replaceAll("<[^>]*>", "");

        String substring = text.substring(0, text.indexOf("\uD83D\uDD04")) +
                "\uD83D\uDD39 Powered by @SkyInfoUzBot \uD83D\uDE80 ";

        String encodedText = URLEncoder.encode(substring, StandardCharsets.UTF_8);

        String shareUrl = "https://t.me/share/url?url=" + encodedText;

        shareButton.setUrl(shareUrl);


        firstRow.add(shareButton);

        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        InlineKeyboardButton editButton = new InlineKeyboardButton();

        editButton.setText(LanguageService.getText(language, "in_btn_edit"));

        editButton.setCallbackData("edit");

        secondRow.add(editButton);

        rows.add(firstRow);
        rows.add(secondRow);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;

    }

    public InlineKeyboardMarkup buildButtonsForHours(User user, int currentPage, List<Hour> hourList) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        int pageSize = 1;
        int totalPages = hourList.size();
        int startIndex = currentPage * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalPages);

        List<InlineKeyboardButton> navbarButtons = new ArrayList<>();

        if (currentPage > 0) {

            InlineKeyboardButton prevButton = new InlineKeyboardButton();

            prevButton.setText("⬅️");

            prevButton.setCallbackData(Const.PREV_BUTTON_PREFIX);

            navbarButtons.add(prevButton);

        }

        InlineKeyboardButton deleteButton = new InlineKeyboardButton();

        deleteButton.setText("❌");

        deleteButton.setCallbackData(Const.DELETE_BUTTON_PREFIX);

        navbarButtons.add(deleteButton);

        if (currentPage < totalPages - 1) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("➡️️");
            nextButton.setCallbackData(Const.NEXT_BUTTON_PREFIX);
            navbarButtons.add(nextButton);
        }

        rows.add(navbarButtons);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
