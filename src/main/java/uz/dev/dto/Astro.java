package uz.dev.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Astro {

    private String sunrise;

    private String sunset;

    private String moonrise;

    @SerializedName("moonset")
    private String moonSet;

    @SerializedName("moon_phase")
    private String moonPhase;

    @SerializedName("moon_illumination")
    private int moonIllumination;

}
