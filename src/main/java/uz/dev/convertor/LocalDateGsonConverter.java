package uz.dev.convertor;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateGsonConverter extends TypeAdapter<LocalDate> {


    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {

        jsonWriter.value(localDate.toString());

    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();

        return LocalDate.parse(string);
    }
}
