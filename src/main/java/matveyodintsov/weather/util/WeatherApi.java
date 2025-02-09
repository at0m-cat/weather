package matveyodintsov.weather.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.exeption.CityNotFoundException;
import matveyodintsov.weather.model.Weather;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class WeatherApi {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode getNode(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new CityNotFoundException("Could not get weather data, status: " + response.getStatusLine().getStatusCode());
            }

            return objectMapper.readTree(response.getEntity().getContent());

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Weather mapToWeather(JsonNode node) {
        try {
            return objectMapper.treeToValue(node, Weather.class);
        } catch (Exception e) {
            throw new CityNotFoundException("Weather data could not be parsed");
        }
    }

}
