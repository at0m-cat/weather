package matveyodintsov.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import matveyodintsov.weather.data.WeatherApiConnect;
import matveyodintsov.weather.data.WeatherData;
import matveyodintsov.weather.exeption.IncorrectCityNameValue;
import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final String key;
    private final String requestByCityUrl;
    private final String requestByLocationUrl;
    private final LocationService locationService;

    public WeatherService(@Value("${weather.api.key}") String apiKey,
                          @Value("${weather.url.geo}") String requestByCityUrl,
                          @Value("${weather.url.weather}") String weatherApiUrl,
                          LocationService locationService) {
        this.key = apiKey;
        this.requestByCityUrl = requestByCityUrl;
        this.requestByLocationUrl = weatherApiUrl;
        this.locationService = locationService;
    }

    public void createLocation(String city, Users user) {
        Location location;
        try {
            location = locationService.findCityLocationInDataBase(city, user);
            location.setUser(user);
        } catch (LocationNotFoundDataBase ignored) {
            JsonNode node = WeatherApiConnect
                    .getNode(urlFindLocation(city));
            location = locationService.createLocation(node, user);
        }
        locationService.save(location);
    }

    public List<WeatherData> getWeatherFromUser(Users user) {
        List<WeatherData> weatherData = new ArrayList<>();
        List<Location> locations = locationService.findAllLocationsFromUser(user);
        for (Location location : locations) {
            weatherData.add(findWeather(location));
        }
        return weatherData;
    }

    private WeatherData findWeather(Location location) {
        JsonNode node = WeatherApiConnect
                .getNode(urlFindCity(location.getLatitude().toString(), location.getLongitude().toString()));
        WeatherData weatherData = mapToWeather(node);
        weatherData.setLocation(location);
        return weatherData;
    }

    private WeatherData mapToWeather(JsonNode node) {
        System.out.println(node.toPrettyString());
        WeatherData weatherData = new WeatherData();
        weatherData.setCityName(node.get("name").asText().toUpperCase());
        weatherData.setIconUrl(node.get("weather").get(0).get("icon").asText());
        weatherData.setTemperature(node.get("main").get("temp").decimalValue());
        weatherData.setDescription(node.get("weather").get(0).get("description").asText());
        weatherData.setFeelsLike(node.get("main").get("feels_like").decimalValue());
        weatherData.setWindSpeed(node.get("wind").get("speed").decimalValue());
        weatherData.setHumidity(node.get("main").get("humidity").decimalValue());
        weatherData.setPressure(node.get("main").get("pressure").decimalValue());
        return weatherData;
    }

    private String urlFindLocation(String city) {
        return requestByCityUrl
                .replace("{city}", city)
                .replace("{key}", key);
    }

    private String urlFindCity(String lat, String lon) {
        return requestByLocationUrl
                .replace("{lat}", lat)
                .replace("{lon}", lon)
                .replace("{key}", key);
    }
}

