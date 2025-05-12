package uz.dev.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.dev.model.User;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardService {

    public static ReplyKeyboardService instance;

    public static ReplyKeyboardService getInstance() {

        if (instance == null) {
            synchronized (ReplyKeyboardService.class) {
                if (instance == null) {
                    instance = new ReplyKeyboardService();
                }
            }
        }
        return instance;
    }


    public ReplyKeyboardMarkup buildReplyButtonForLocation(User user) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton();

        String text = LanguageService.getText(user.getLanguage(), "btn_location");

        locationButton.setText(text);
        locationButton.setRequestLocation(true);

        row.add(locationButton);
        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup buildButtonsForMenu(User user) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton currentButton = new KeyboardButton();

        currentButton.setText(LanguageService.getText(user.getLanguage(), "btn_current"));

        row.add(currentButton);
        keyboardRows.add(row);

        KeyboardRow secondKeyboardRow = new KeyboardRow();

        KeyboardButton clockButton = new KeyboardButton();

        clockButton.setText(LanguageService.getText(user.getLanguage(), "btn_clock"));

        secondKeyboardRow.add(clockButton);

        KeyboardButton astroButton = new KeyboardButton();

        astroButton.setText(LanguageService.getText(user.getLanguage(), "btn_astro"));

        secondKeyboardRow.add(astroButton);

        keyboardRows.add(secondKeyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;

    }

    public ReplyKeyboardMarkup buildCancelButton(User user) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton cancelButton = new KeyboardButton();

        cancelButton.setText(LanguageService.getText(user.getLanguage(), "btn_cancel"));

        row.add(cancelButton);
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;

    }
}
