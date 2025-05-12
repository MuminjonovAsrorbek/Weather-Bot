package uz.dev.dto.jsonDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dev.dto.Current;
import uz.dev.dto.Forecast;
import uz.dev.dto.Location;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForecastDTO {

    private Location location;

    private Current current;

    private Forecast forecast;

}
