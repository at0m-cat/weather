package matveyodintsov.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.data.WeatherData;
import matveyodintsov.weather.dto.LocationDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final String key;
    private final String requestByCityUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherService(@Value("${weather.api.key}") String apiKey,
                          @Value("${weather.url.geo}") String requestByCityUrl) {
        this.key = apiKey;
        this.requestByCityUrl = requestByCityUrl;
    }

    public List<WeatherData> getDefaultWeatherData() {
        List<WeatherData> weatherDataList = new ArrayList<>();
        weatherDataList.add(getWeather("krasnoyarsk"));
        weatherDataList.add(getWeather("zelenogorsk"));
        weatherDataList.add(getWeather("moscow"));
        weatherDataList.add(getWeather("yakutsk"));
        return weatherDataList;
    }

    public WeatherData getWeather(String city) {
        String url = requestByCityUrl
                .replace("{city}", city)
                .replace("{key}", key);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
            JsonNode node = objectMapper.readTree(response.getEntity().getContent());
            return mapJsonToWeatherData(node);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private WeatherData mapJsonToWeatherData(JsonNode node) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCityName(node.get("name").asText());
        weatherData.setIconUrl(node.get("weather").get(0).get("icon").asText());
        weatherData.setTemperature(node.get("main").get("temp").decimalValue());
        weatherData.setDescription(node.get("weather").get(0).get("description").asText());
        weatherData.setFeelsLike(node.get("main").get("feels_like").decimalValue());
        weatherData.setWindSpeed(node.get("wind").get("speed").decimalValue());
        weatherData.setHumidity(node.get("main").get("humidity").decimalValue());
        weatherData.setPressure(node.get("main").get("pressure").decimalValue());
        LocationDto locationDto = new LocationDto();
        locationDto.setLongitude(node.get("coord").get("lon").decimalValue());
        locationDto.setLatitude(node.get("coord").get("lat").decimalValue());
        weatherData.setLocation(locationDto);
        return weatherData;
    }
}

