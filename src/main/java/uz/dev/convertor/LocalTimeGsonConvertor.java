package uz.dev.convertor;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeGsonConvertor extends TypeAdapter<LocalTime> {
    @Override
    public void write(JsonWriter jsonWriter, LocalTime localTime) throws IOException {
        jsonWriter.value(localTime.toString());
    }

    @Override
    public LocalTime read(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        return LocalTime.parse(string);
    }
}
