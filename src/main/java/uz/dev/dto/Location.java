package uz.dev.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {

    @SerializedName("name")
    private String name;

    @SerializedName("region")
    private String region;

    @SerializedName("country")
    private String country;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    @SerializedName("tz_id")
    private ZoneId timezone;

    @SerializedName("localtime_epoch")
    private Long timeEpoch;

    @SerializedName("localtime")
    private LocalDateTime date;

}
