package uz.dev.dto.jsonDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dev.dto.Current;
import uz.dev.dto.Location;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrentDTO {

    private Location location;

    private Current current;

}
