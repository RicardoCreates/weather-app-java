import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class WeatherApp {

    private static final String API_KEY = "d3d22fc98f5ce6a5482724302f3267a9";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        String city = "Berlin"; // Die Stadt, für die du das Wetter abrufen möchtest
        try {
            String response = getWeatherData(city);
            if (response != null) {
                parseAndDisplayWeatherData(response);
            } else {
                System.out.println("Keine Daten empfangen.");
            }
        } catch (IOException | ParseException e) {  // ParseException hinzugefügt
            System.out.println("Fehler beim Abrufen der Wetterdaten: " + e.getMessage());
        }
    }

    private static String getWeatherData(String city) throws IOException, ParseException {
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    private static void parseAndDisplayWeatherData(String responseBody) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        String cityName = jsonObject.get("name").getAsString();
        JsonObject main = jsonObject.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        int humidity = main.get("humidity").getAsInt();

        JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        String description = weather.get("description").getAsString();

        System.out.println("Wetter in " + cityName + ":");
        System.out.println("Temperatur: " + temperature + "°C");
        System.out.println("Feuchtigkeit: " + humidity + "%");
        System.out.println("Beschreibung: " + description);
    }
}
