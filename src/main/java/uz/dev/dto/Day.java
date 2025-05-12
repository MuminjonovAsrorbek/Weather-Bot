package uz.dev.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Day {

    @SerializedName("maxtemp_c")
    private Double maxTempCelsius;

    @SerializedName("maxtemp_f")
    private Double maxTempFahrenheit;

    @SerializedName("mintemp_c")
    private Double minTempCelsius;

    @SerializedName("mintemp_f")
    private Double minTempFahrenheit;

    @SerializedName("avgtemp_c")
    private Double avgTempCelsius;

    @SerializedName("avgtemp_f")
    private Double avgTempFahrenheit;

    @SerializedName("totalprecip_mm")
    private Double totalPrecipitationMillimeters;

    @SerializedName("totalprecip_in")
    private Double totalPrecipitationInches;

    @SerializedName("totalsnow_cm")
    private Double totalSnowCentimeters;

    private Double avgHumidity;

    private Condition condition;

    @SerializedName("daily_chance_of_rain")
    private int dailyChanceOfRain; //Yomg'ir ehtimoli foiz sifatida

    @SerializedName("daily_chance_of_snow")
    private int dailyChanceOfSnow; // Qor yog'ishi ehtimoli foiz sifatida

}
