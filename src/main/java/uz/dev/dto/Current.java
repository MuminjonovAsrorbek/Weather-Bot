package uz.dev.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Current {

    @SerializedName("last_updated_epoch")
    private String lastUpdateEpoch;

    @SerializedName("last_updated")
    private LocalDateTime lastUpdate;

    @SerializedName("temp_c")
    private Double tempCelsius;

    @SerializedName("temp_f")
    private Double tempFahrenheit;

    private Condition condition;

    @SerializedName("wind_mph")
    private Double windSpeedMilesPerHour;

    @SerializedName("wind_kph")
    private Double windSpeedKilometersPerHour;

    @SerializedName("precip_mm")
    private Double precipitationAmountMillimeters; // Yog'ingarchilik miqdori millimetrda

    @SerializedName("precip_in")
    private Double precipitationAmountInches; //Dyuymdagi yog'ingarchilik miqdori

    private int humidity; //Namlik foiz sifatida

    private int cloud; //Bulutlilik foiz sifatida




}
