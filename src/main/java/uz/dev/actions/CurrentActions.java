package uz.dev.actions;

import lombok.Data;
import uz.dev.connect.HttpConnect;
import uz.dev.dto.Condition;
import uz.dev.dto.Current;
import uz.dev.dto.Location;
import uz.dev.dto.jsonDTO.CurrentDTO;

@Data
public class CurrentActions {

    private static CurrentDTO currentsDTO;

    private static HttpConnect connect;

    public CurrentActions(String apiUrl) {

        connect = new HttpConnect(apiUrl);

        currentsDTO = connect.getCurrentJson();

    }

    public Location getLocation() {

        return currentsDTO.getLocation();

    }

    public Current getCurrent() {

        return currentsDTO.getCurrent();

    }

    public Condition getCondition() {
        return getCurrent().getCondition();
    }


}
