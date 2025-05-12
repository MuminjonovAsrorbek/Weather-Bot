package uz.dev.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForecastDay {

    private LocalDate date;

    @SerializedName("date_epoch")
    private Long dateEpoch;

    private Day day;

    private Astro astro;

    private List<Hour> hour;

}
