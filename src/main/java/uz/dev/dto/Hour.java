package uz.dev.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hour {

    @SerializedName("time_epoch")
    private Long timeEpoch;

    private LocalDateTime time;

    @SerializedName("temp_c")
    private Double tempCelsius;

    @SerializedName("temp_f")
    private Double tempFahrenheit;

    private Condition condition;

    @SerializedName("wind_mph")
    private Double maxWindSpeedMilesPerHour;

    @SerializedName("wind_kph")
    private Double windSpeedKilometersPerHour;

    @SerializedName("pressure_mb")
    private Double pressureMillibars;

    @SerializedName("pressure_in")
    private Double pressureInches;

    @SerializedName("precip_mm")
    private Double precipitationAmountMillimeters;

    @SerializedName("precip_in")
    private Double precipitationAmountInches;

    @SerializedName("snow_cm")
    private Double snowCentimeters;

    private Double humidity;

    private Double cloud;

    @SerializedName("chance_of_rain")
    private int chanceOfRain;

    @SerializedName("chance_of_snow")
    private int chanceOfSnow;

}
