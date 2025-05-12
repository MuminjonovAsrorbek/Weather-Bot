package uz.dev.process;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import uz.dev.actions.CurrentActions;
import uz.dev.actions.ForecastActions;
import uz.dev.connect.HttpConnect;
import uz.dev.dto.Astro;
import uz.dev.dto.Current;
import uz.dev.dto.Hour;
import uz.dev.dto.Location;
import uz.dev.model.User;
import uz.dev.service.BotService;
import uz.dev.service.InlineButtonService;
import uz.dev.service.LanguageService;
import uz.dev.service.ReplyKeyboardService;
import uz.dev.utils.Const;
import uz.dev.utils.Step;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessMessage {

    public static ProcessMessage instance;

    private static final BotService botService = BotService.getInstance();
    private static final InlineButtonService inlineButtonService = InlineButtonService.getInstance();

    private final Map<String, User> users = BotService.getInstance().getUsers();

    public static ProcessMessage getInstance() {
        if (instance == null) {
            synchronized (ProcessMessage.class) {
                if (instance == null) {
                    instance = new ProcessMessage();
                }
            }
        }
        return instance;
    }


    public void process(Message message, User user) {

        if (message.hasText()) {

            Long chatId = message.getChatId();

            String text = message.getText();

            if (text.equals("/start")) {

                if (user.getLanguage().isEmpty()) {

                    String sendText = """
                            \uD83D\uDD39 Iltimos, tilni tanlang: \uD83C\uDDFA\uD83C\uDDFF O‘zbekcha
                            \uD83D\uDD39 Please select a language: \uD83C\uDDEC\uD83C\uDDE7 English
                            \uD83D\uDD39 Пожалуйста, выберите язык: \uD83C\uDDF7\uD83C\uDDFA Русский""";

                    InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtonService.buildInlineButtonForSelectLanguages();

                    botService.sendMessage(chatId, sendText, inlineKeyboardMarkup);

                    user.setStep(Step.SELECT_LANG);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);
                } else {

                    String language = user.getLanguage();

                    String sendText = LanguageService.getText(language, "start");

                    ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardService.getInstance().buildButtonsForMenu(user);

                    user.setStep(Step.SELECT_MENU);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);

                    botService.sendMessage(chatId, sendText, replyKeyboardMarkup);

                }
            } else if (user.getStep().equals(Step.SELECT_MENU)) {

                String language = user.getLanguage();

                language = LanguageService.getText(language, "btn_current");

                if (text.equals(language)) {

                    String format = sendCurrentWeather(user);

                    InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtonService.buildButtonsForCurrentWeather(user);

                    botService.sendMessage(chatId, format, inlineKeyboardMarkup);

                }

                String astroButton = LanguageService.getText(user.getLanguage(), "btn_astro");

                if (text.equals(astroButton)) {

                    String sendText = sendAstro(user);

                    botService.sendMessage(chatId, sendText);

                }

                String clockButton = LanguageService.getText(user.getLanguage(), "btn_clock");

                if (text.equals(clockButton)) {

                    sendClockWeather(user);

                }

                if (text.equals("/lang")) {

                    String sendText = """
                            \uD83D\uDD39 Iltimos, tilni tanlang: \uD83C\uDDFA\uD83C\uDDFF O‘zbekcha
                            \uD83D\uDD39 Please select a language: \uD83C\uDDEC\uD83C\uDDE7 English
                            \uD83D\uDD39 Пожалуйста, выберите язык: \uD83C\uDDF7\uD83C\uDDFA Русский""";

                    InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtonService.buildInlineButtonForSelectLanguages();

                    botService.sendMessage(chatId, sendText, inlineKeyboardMarkup);

                    user.setStep(Step.SELECT_LANG);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);

                }

                if (text.equals("/location")) {

                    String sendText = LanguageService.getText(user.getLanguage(), "send_location");

                    ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardService.getInstance().buildReplyButtonForLocation(user);

                    botService.sendMessage(chatId, sendText, replyKeyboardMarkup);

                    user.setStep(Step.SEND_LOCATION);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);
                }

            } else if (user.getStep().equals(Step.ENTER_NEW_CITY)) {

                String language = LanguageService.getText(user.getLanguage(), "btn_cancel");

                if (text.equals(language)) {

                    String sendMessage = LanguageService.getText(user.getLanguage(), "menu");

                    user.setStep(Step.SELECT_MENU);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);

                    botService.sendMessage(chatId, sendMessage, ReplyKeyboardService.getInstance().buildButtonsForMenu(user));

                } else {

                    if (isValidCityName(text)) {

                        String apiUrl = botService.getApiUrl(Const.CURRENT, text, user.getLanguage());

                        HttpConnect httpConnect = new HttpConnect(apiUrl);

                        int statusCode = httpConnect.getStatusCode();

                        if (statusCode == 200) {

                            String sendText = LanguageService.getText(user.getLanguage(), "save_city");

                            List<String> locations = new ArrayList<>();
                            locations.add(text);

                            user.setLocations(locations);
                            user.setStep(Step.SELECT_MENU);

                            users.put(message.getChatId().toString(), user);

                            botService.setUsers(users);

                            botService.sendMessage(chatId, sendText, ReplyKeyboardService.getInstance().buildButtonsForMenu(user));


                        } else {

                            String sendText = LanguageService.getText(user.getLanguage(), "401_error");

                            botService.sendMessage(chatId, sendText);

                        }


                    } else {

                        String sendText = LanguageService.getText(user.getLanguage(), "invalid_format");

                        botService.sendMessage(chatId, sendText);

                    }

                }

            }
        } else if (message.hasLocation()) {

            if (user.getStep().equals(Step.SEND_LOCATION)) {

                org.telegram.telegrambots.meta.api.objects.Location location = message.getLocation();

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                String apiUrl = "https://api.weatherapi.com/v1/current.json?q=%s,%s&lang=%s";

                String formatted = String.format(apiUrl, latitude, longitude, user.getLanguage());

                HttpConnect httpConnect = new HttpConnect(formatted);

                int statusCode = httpConnect.getStatusCode();

                if (statusCode == 200) {

                    String sendText = LanguageService.getText(user.getLanguage(), "save_city");

                    ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardService.getInstance().buildButtonsForMenu(user);

                    botService.sendMessage(user.getUserId(), sendText, replyKeyboardMarkup);

                    CurrentActions currentActions = new CurrentActions(formatted);

                    String name = currentActions.getLocation().getName();

                    List<String> list = new ArrayList<>();

                    list.add(name);

                    user.setLocations(list);
                    user.setStep(Step.SELECT_MENU);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);

                } else {

                    String sendText = LanguageService.getText(user.getLanguage(), "location_not_found");
                    user.setStep(Step.SELECT_MENU);

                    users.put(message.getChatId().toString(), user);

                    botService.setUsers(users);

                    botService.sendMessage(user.getUserId(), sendText, ReplyKeyboardService.getInstance().buildButtonsForMenu(user));

                }

            }

        }

    }

    public List<Hour> getHours(User user) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String apiUrl = botService.getApiUrl(Const.FORECAST, user.getLocations().getFirst(), user.getLanguage());

        ForecastActions forecastActions = new ForecastActions(apiUrl);

        LocalDateTime date = forecastActions.getLocation().getDate();

        String formatDate = formatter.format(date);

        return forecastActions.getHour(LocalDate.parse(formatDate));

    }

    public void sendClockWeather(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String apiUrl = botService.getApiUrl(Const.FORECAST, user.getLocations().getFirst(), user.getLanguage());

        ForecastActions forecastActions = new ForecastActions(apiUrl);

        LocalDateTime date = forecastActions.getLocation().getDate();

        String formatDate = formatter.format(date);

        List<Hour> hourList = forecastActions.getHour(LocalDate.parse(formatDate));

        formatter = DateTimeFormatter.ofPattern("HH");

        String hourFormat = formatter.format(date);

        int listIndex = getListIndex(hourList, hourFormat);

        user.setCurrentPage(listIndex);

        users.put(user.getUserId().toString(), user);

        botService.setUsers(users);

        InlineKeyboardMarkup inlineKeyboardMarkup = inlineButtonService.buildButtonsForHours(user, listIndex, hourList);

        String string = makeClockWeatherText(hourList, listIndex, user);

        botService.sendMessage(user.getUserId(), string, inlineKeyboardMarkup);
    }

    public String makeClockWeatherText(List<Hour> hourList, int listIndex, User user) {

        Hour hour = hourList.get(listIndex);

        String sendText = LanguageService.getText(user.getLanguage(), "inf_btn_clock");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime time = hour.getTime();

        String formatTime = formatter.format(time);

        Double tempCelsius = hour.getTempCelsius();

        String conditionText = hour.getCondition().getText();

        Double windSpeedKilometersPerHour = hour.getWindSpeedKilometersPerHour();

        Double humidity = hour.getHumidity();

        Double cloud = hour.getCloud();

        int chanceOfRain = hour.getChanceOfRain();

        int chanceOfSnow = hour.getChanceOfSnow();

        return String.format(sendText, formatTime, tempCelsius, conditionText, windSpeedKilometersPerHour,
                humidity, cloud, chanceOfRain, chanceOfSnow);

    }

    private int getListIndex(List<Hour> hourList, String hourFormat) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

        for (Hour hour : hourList) {

            LocalDateTime time = hour.getTime();

            String formatted = formatter.format(time);

            if (formatted.equals(hourFormat)) {

                return hourList.indexOf(hour);

            }

        }

        return 0;

    }

    public String sendAstro(User user) {
        String apiUrl = botService.getApiUrl(Const.FORECAST, user.getLocations().getFirst(), user.getLanguage());

        ForecastActions forecastActions = new ForecastActions(apiUrl);

        String sendText = LanguageService.getText(user.getLanguage(), "inf_btn_astro");

        sendText = sendText.replaceAll("\\\\n", "\n");

        LocalDateTime date = forecastActions.getLocation().getDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String format = formatter.format(date);

        Astro astro = forecastActions.getAstro(LocalDate.parse(format));

        String sunrise = astro.getSunrise();

        String sunset = astro.getSunset();

        String moonrise = astro.getMoonrise();

        String moonSet = astro.getMoonSet();

        String moonPhase = astro.getMoonPhase();

        int moonIllumination = astro.getMoonIllumination();

        return String.format(sendText, sunrise, sunset, moonrise, moonSet, moonIllumination, moonPhase);
    }

    public boolean isValidCityName(String city) {
        return city.matches("^[A-Za-z]+$");
    }

    public String sendCurrentWeather(User user) {
        String sendText = LanguageService.getText(user.getLanguage(), "inf_btn_current");

        sendText = sendText.replaceAll("\\\\n", "\n");

        String apiUrl = botService.getApiUrl(Const.CURRENT, user.getLocations().getFirst(), user.getLanguage());

        CurrentActions currentActions = new CurrentActions(apiUrl);

        Location location = currentActions.getLocation();

        String region = location.getRegion();

        String country = location.getCountry();

        ZoneId timezone = location.getTimezone();

        LocalDateTime date = LocalDateTime.now();

        Current current = currentActions.getCurrent();

        LocalDateTime lastUpdate = current.getLastUpdate();

        Double tempCelsius = current.getTempCelsius();

        String conditionText = current.getCondition().getText();

        Double windSpeedKilometersPerHour = current.getWindSpeedKilometersPerHour();

        Double precipitationAmountMillimeters = current.getPrecipitationAmountMillimeters();

        int humidity = current.getHumidity();

        int cloud = current.getCloud();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatDate = formatter.format(date);

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return String.format(sendText, region, country, timezone, formatDate, formatter.format(lastUpdate),
                tempCelsius, conditionText, windSpeedKilometersPerHour, precipitationAmountMillimeters,
                humidity, cloud);

    }

}
