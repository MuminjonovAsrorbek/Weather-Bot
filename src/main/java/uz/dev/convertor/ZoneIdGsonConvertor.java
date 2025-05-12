package uz.dev.convertor;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZoneId;

public class ZoneIdGsonConvertor extends TypeAdapter<ZoneId> {
    @Override
    public void write(JsonWriter jsonWriter, ZoneId zoneId) throws IOException {
        jsonWriter.value(zoneId.toString());
    }

    @Override
    public ZoneId read(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();

        return ZoneId.of(string);
    }
}
