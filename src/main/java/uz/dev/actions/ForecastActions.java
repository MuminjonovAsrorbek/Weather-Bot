package uz.dev.actions;

import lombok.Data;
import uz.dev.connect.HttpConnect;
import uz.dev.dto.*;
import uz.dev.dto.jsonDTO.ForecastDTO;

import java.time.LocalDate;
import java.util.List;

@Data
public class ForecastActions {

    private static ForecastDTO forecastDTO;

    private static HttpConnect httpConnect;

    public ForecastActions(String apiUrl) {

        httpConnect = new HttpConnect(apiUrl);

        forecastDTO = httpConnect.getForecastJson();

    }

    public Location getLocation() {
        return forecastDTO.getLocation();
    }

    public Current getCurrent() {
        return forecastDTO.getCurrent();
    }

    public Forecast getForecast() {
        return forecastDTO.getForecast();
    }

    public Condition getCondition() {
        return getCurrent().getCondition();
    }

    public List<ForecastDay> getForecastDays() {
        return getForecast().getForecastDay();
    }

    public Day getDay(LocalDate date) {
        List<ForecastDay> forecastDays = getForecastDays();

        for (ForecastDay forecastDay : forecastDays) {

            if (forecastDay.getDate().isEqual(date)) {
                return forecastDay.getDay();
            }

        }
        return null;
    }

    public Condition getConditionInDay(LocalDate date) {

        Day day = getDay(date);

        if (day == null) {

            return null;

        }

        return getCondition();

    }

    public Astro getAstro(LocalDate date) {

        List<ForecastDay> forecastDays = getForecastDays();

        for (ForecastDay forecastDay : forecastDays) {

            if (forecastDay.getDate().isEqual(date)) {

                return forecastDay.getAstro();

            }

        }

        return null;

    }

    public List<Hour> getHour(LocalDate date) {

        List<ForecastDay> forecastDays = getForecastDays();

        for (ForecastDay forecastDay : forecastDays) {

            if (forecastDay.getDate().isEqual(date)) {

                return forecastDay.getHour();

            }

        }

        return null;

    }


}
