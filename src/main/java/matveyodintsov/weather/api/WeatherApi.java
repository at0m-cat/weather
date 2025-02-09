package matveyodintsov.weather.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.exeption.CityNotFoundException;
import matveyodintsov.weather.model.Weather;
import matveyodintsov.weather.util.Mapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherApi implements Api<Weather> {

    private final ObjectMapper objectMapper;
    private final String key;
    private final String findLocationByCityUrl;
    private final String findLocationByLocationUrl;

    @Autowired
    public WeatherApi(@Value("${weather.api.key}") String key,
                      @Value("${weather.url.geo}") String findLocationByCityUrl,
                      @Value("${weather.url.weather}") String findCityByLocationUrl) {
        this.objectMapper = new ObjectMapper();
        this.key = key;
        this.findLocationByCityUrl = findLocationByCityUrl;
        this.findLocationByLocationUrl = findCityByLocationUrl;
    }

    @Override
    public Weather getWeatherByLocation(String lat, String lon) {
        String url = findLocationByLocationUrl
                .replace("{lat}", lat)
                .replace("{lon}", lon)
                .replace("{key}", key);
        JsonNode response = getNode(url);
        return Mapper.WeatherMapper.mapJsonToWeather(response);
    }

    @Override
    public Weather getWeatherByCity(String city) {
        String url = findLocationByCityUrl
                .replace("{city}", city)
                .replace("{key}", key);
        JsonNode response = getNode(url);
        return Mapper.WeatherMapper.mapJsonToWeather(response);
    }

    private JsonNode getNode(String url) {
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

}
