package uz.dev.connect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import uz.dev.convertor.LocalDateGsonConverter;
import uz.dev.convertor.LocalDateTimeGsonConvertor;
import uz.dev.convertor.LocalTimeGsonConvertor;
import uz.dev.convertor.ZoneIdGsonConvertor;
import uz.dev.dto.jsonDTO.CurrentDTO;
import uz.dev.dto.jsonDTO.ForecastDTO;
import uz.dev.utils.Config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class HttpConnect {

    private static String API_URL;

    private static final String API_KEY = Config.API_KEY;

    public HttpConnect(String apiUrl) {

        API_URL = apiUrl + "&key=" + API_KEY;

    }

    public CurrentDTO getCurrentJson() {

        try {

            Result result = getResult();

            return result.gson().fromJson(result.body(), CurrentDTO.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public ForecastDTO getForecastJson() {

        try {

            Result result = getResult();

            return result.gson().fromJson(result.body(), ForecastDTO.class);


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    private static Result getResult() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(API_URL);

        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        HttpResponse.BodyHandler<String> response = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> send = client.send(request, response);

        String body = send.body();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateGsonConverter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonConvertor())
                .registerTypeAdapter(LocalTime.class, new LocalTimeGsonConvertor())
                .registerTypeAdapter(ZoneId.class, new ZoneIdGsonConvertor())
                .setPrettyPrinting().create();

        System.out.println(send.statusCode());
        return new Result(body, gson);
    }

    private record Result(String body, Gson gson) {
    }

    public int getStatusCode(){

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(API_URL);

        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        HttpResponse.BodyHandler<String> response = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> send = client.send(request, response);

            return send.statusCode();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {

        HttpConnect httpConnect = new HttpConnect("https://api.weatherapi.com/v1/forecast.json?q=Tashkent&days=6&");

        ForecastDTO currentJson = httpConnect.getForecastJson();

        System.out.println(currentJson);

    }

}
